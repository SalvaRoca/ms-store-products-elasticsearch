package com.bassmania.msstoreproductselasticsearch.service;

import com.bassmania.msstoreproductselasticsearch.model.db.Product;
import com.bassmania.msstoreproductselasticsearch.model.response.ProductsQueryResponse;

public interface ProductsService {

	ProductsQueryResponse getProducts(String productRef, String category, String brand, String model, String description, Double priceMin, Double priceMax, String aggregate);

	Product getProduct(String productRef);

}
