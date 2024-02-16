package com.bassmania.msstoreproductselasticsearch.service;

import com.bassmania.msstoreproductselasticsearch.data.DataAccessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.bassmania.msstoreproductselasticsearch.model.Product;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductsServiceImpl implements ProductsService {

	private final DataAccessRepository dataAccessRepository;

	@Override
	public List<Product> getProducts(String productRef, String category, String brand, String model, String description) {
		return dataAccessRepository.findProducts(productRef, category, brand, model, description);
	}

	@Override
	public Product getProduct(String productRef) {
		return dataAccessRepository.findByProductRef(productRef).orElse(null);
	}
}
