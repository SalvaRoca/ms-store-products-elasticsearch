package com.bassmania.msstoreproductselasticsearch.model.response;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class BrandAggregationDetails extends AggregationDetails {

    private String key;
    private Integer count;

}
