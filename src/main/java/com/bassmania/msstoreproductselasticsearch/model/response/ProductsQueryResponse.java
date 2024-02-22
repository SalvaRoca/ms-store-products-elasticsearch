package com.bassmania.msstoreproductselasticsearch.model.response;


import com.bassmania.msstoreproductselasticsearch.model.db.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProductsQueryResponse {

    private List<Product> products;
    private List<AggregationDetails> aggs;

}
