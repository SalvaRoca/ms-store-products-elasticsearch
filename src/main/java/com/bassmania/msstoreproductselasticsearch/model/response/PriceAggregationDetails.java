package com.bassmania.msstoreproductselasticsearch.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class PriceAggregationDetails extends AggregationDetails {

    private Double minPrice;
    private Double maxPrice;

}
