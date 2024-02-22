package com.bassmania.msstoreproductselasticsearch.service;

import com.bassmania.msstoreproductselasticsearch.data.DataAccessRepository;
import com.bassmania.msstoreproductselasticsearch.model.response.ProductsQueryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.bassmania.msstoreproductselasticsearch.model.db.Product;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

	private final DataAccessRepository dataAccessRepository;

	@Override
	public ProductsQueryResponse getProducts(String productRef, String category, String brand, String model, String description, Double priceMin, Double priceMax, String aggregate) {
		return dataAccessRepository.findProducts(productRef, category, brand, model, description, priceMin, priceMax, aggregate);
	}

	@Override
	public Product getProduct(String productRef) {
		return dataAccessRepository.findByProductRef(productRef).orElse(null);
	}
}
