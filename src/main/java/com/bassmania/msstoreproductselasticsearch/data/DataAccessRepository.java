package com.bassmania.msstoreproductselasticsearch.data;

import java.util.*;

import com.bassmania.msstoreproductselasticsearch.model.Product;
import lombok.SneakyThrows;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilders;
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
    public List<Product> findProducts(String productRef, String category, String brand, String model, String description) {

        BoolQueryBuilder querySpec = QueryBuilders.boolQuery();

        if (!StringUtils.isEmpty(productRef)) {
            querySpec.must(QueryBuilders.termQuery("productRef", productRef));
        }

        if (!StringUtils.isEmpty(category)) {
            querySpec.must(QueryBuilders.matchQuery("category", category));
        }

        if (!StringUtils.isEmpty(brand)) {
            querySpec.must(QueryBuilders.matchQuery("brand", brand));
        }

        if (!StringUtils.isEmpty(model)) {
            querySpec.must(QueryBuilders.matchQuery("model", model));
        }

        if (!StringUtils.isEmpty(description)) {
            querySpec.must(QueryBuilders.multiMatchQuery(description, descriptionSearchFields).type(Type.BOOL_PREFIX));
        }

        if (!querySpec.hasClauses()) {
            querySpec.must(QueryBuilders.matchAllQuery());
        }

        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder().withQuery(querySpec);

        Query query = nativeSearchQueryBuilder.build();
        SearchHits<Product> result = elasticClient.search(query, Product.class);

        return result.getSearchHits().stream().map(SearchHit::getContent).toList();
    }

}
