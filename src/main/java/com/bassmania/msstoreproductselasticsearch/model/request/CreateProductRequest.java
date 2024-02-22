package com.bassmania.msstoreproductselasticsearch.model.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateProductRequest {
	private String productRef;
	private String category;
	private String brand;
	private String model;
	private String description;
	private String img;
	private Double price;
}
