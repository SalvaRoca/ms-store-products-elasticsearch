package com.bassmania.msstoreproductselasticsearch.controller;

import com.bassmania.msstoreproductselasticsearch.model.response.ProductsQueryResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bassmania.msstoreproductselasticsearch.model.db.Product;
import com.bassmania.msstoreproductselasticsearch.service.ProductsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

	private final ProductsService productsService;

	@GetMapping
	public ResponseEntity<ProductsQueryResponse> getProducts(
			@RequestParam(required = false) String productRef,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String description,
			@RequestParam(required = false) Double minPrice,
			@RequestParam(required = false) Double maxPrice,
			@RequestParam(required = false) String aggregate
	) {

		ProductsQueryResponse productList = productsService.getProducts(productRef, category, brand, model, description, minPrice, maxPrice, aggregate);
		return ResponseEntity.ok(productList);
	}

	@GetMapping("/{productRef}")
	public ResponseEntity<Product> getProduct(@PathVariable String productRef) {

		Product product = productsService.getProduct(productRef);

		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
