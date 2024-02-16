package com.bassmania.msstoreproductselasticsearch.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.*;

@Document(indexName = "products")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class Product {
	@Id
	private String id;

	@Field(type = FieldType.Keyword, name = "productRef")
	private String productRef;

	@Field(type = FieldType.Keyword, name = "category")
	private String category;

	@Field(type = FieldType.Text, name = "brand")
	private String brand;

	@Field(type = FieldType.Text, name = "model")
	private String model;

	@Field(type = FieldType.Search_As_You_Type, name = "description")
	private String description;

	@Field(type = FieldType.Keyword, name = "img")
	private String img;

	@Field(type = FieldType.Double, name = "price")
	private Double price;
}