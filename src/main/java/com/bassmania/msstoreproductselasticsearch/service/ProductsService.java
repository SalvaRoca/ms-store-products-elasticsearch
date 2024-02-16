package com.bassmania.msstoreproductselasticsearch.service;

import com.bassmania.msstoreproductselasticsearch.model.Product;

import java.util.List;

public interface ProductsService {

	List<Product> getProducts(String productRef, String category, String brand, String model, String description);
	
	Product getProduct(String productRef);

}
