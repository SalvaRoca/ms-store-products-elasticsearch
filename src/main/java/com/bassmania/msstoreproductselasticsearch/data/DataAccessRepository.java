package com.bassmania.msstoreproductselasticsearch.data;

import java.util.*;

import com.bassmania.msstoreproductselasticsearch.model.db.Product;
import com.bassmania.msstoreproductselasticsearch.model.response.AggregationDetails;
import com.bassmania.msstoreproductselasticsearch.model.response.BrandAggregationDetails;
import com.bassmania.msstoreproductselasticsearch.model.response.PriceAggregationDetails;
import com.bassmania.msstoreproductselasticsearch.model.response.ProductsQueryResponse;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms;
import org.elasticsearch.search.aggregations.metrics.Max;
import org.elasticsearch.search.aggregations.metrics.Min;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Repository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Repository
@RequiredArgsConstructor
@Slf4j
public class DataAccessRepository {

    private final ProductRepository productRepository;
    private final ElasticsearchOperations elasticClient;

    private final String[] descriptionSearchFields = {"description", "description._2gram", "description._3gram"};

	public Optional<Product> findByProductRef(String productRef) {
		return productRepository.findByProductRef(productRef);
	}

    @SneakyThrows
    public ProductsQueryResponse findProducts(String productRef, String category, String brand, String model, String description, Double minPrice, Double maxPrice, String aggregate) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(productRef)) {
            querySpec.must(QueryBuilders.termQuery("productRef", productRef));
        }

        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.termQuery("category", category));
        }

        if (!StringUtils.isEmpty(brand)) {
            querySpec.must(QueryBuilders.termQuery("brand", brand));
        }

        if (!StringUtils.isEmpty(model)) {
            querySpec.must(QueryBuilders.matchQuery("model", model));
        }

        if (!StringUtils.isEmpty(description)) {
            querySpec.must(QueryBuilders.multiMatchQuery(description, descriptionSearchFields).type(Type.BOOL_PREFIX));
        }

        if (minPrice != null || maxPrice != null) {
            BoolQueryBuilder priceRangeQuery = QueryBuilders.boolQuery();
            if (minPrice != null) {
                priceRangeQuery.must(QueryBuilders.rangeQuery("price").gte(minPrice));
            }
            if (maxPrice != null) {
                priceRangeQuery.must(QueryBuilders.rangeQuery("price").lte(maxPrice));
            }
            querySpec.must(priceRangeQuery);
        }

        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        if (StringUtils.equals(aggregate, "brand")) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.terms("Brand Aggregation").field("brand").size(1000));
            nativeSearchQueryBuilder.withMaxResults(0);
        }

        if (StringUtils.equals(aggregate, "price")) {
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.min("minPrice").field("price"));
            nativeSearchQueryBuilder.addAggregation(AggregationBuilders.max("maxPrice").field("price"));
            nativeSearchQueryBuilder.withMaxResults(0);
        }

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Product> result = elasticClient.search(query, Product.class);

        List<AggregationDetails> responseAggs = new LinkedList<>();

        if (result.hasAggregations()) {
            Map<String, Aggregation> aggs = Objects.requireNonNull(result.getAggregations()).asMap();

            if (StringUtils.equals(aggregate, "brand")) {
                ParsedStringTerms brandAgg = (ParsedStringTerms) aggs.get("Brand Aggregation");

                brandAgg.getBuckets()
                        .forEach(
                                bucket -> responseAggs.add(
                                        BrandAggregationDetails.builder()
                                                .key(bucket.getKeyAsString())
                                                .count((int) bucket.getDocCount())
                                                .build()
                                ));
            }

            if (StringUtils.equals(aggregate, "price")) {
                Double minPriceAgg = ((Min) aggs.get("minPrice")).getValue();
                Double maxPriceAgg = ((Max) aggs.get("maxPrice")).getValue();

                responseAggs.add(
                        PriceAggregationDetails.builder()
                                .minPrice(minPriceAgg)
                                .maxPrice(maxPriceAgg)
                                .build()
                );
            }
        }

        return new ProductsQueryResponse(result.getSearchHits().stream().map(SearchHit::getContent).toList(), responseAggs);
    }
}
