package com.bassmania.msstoreproductselasticsearch.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.bassmania.msstoreproductselasticsearch.model.Product;
import com.bassmania.msstoreproductselasticsearch.service.ProductsService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductsController {

	private final ProductsService service;

	@GetMapping
	public ResponseEntity<List<Product>> getProducts(
			@RequestParam(required = false) String productRef,
			@RequestParam(required = false) String category,
			@RequestParam(required = false) String brand,
			@RequestParam(required = false) String model,
			@RequestParam(required = false) String description
	) {

		List<Product> productList = service.getProducts(productRef, category, brand, model, description);
		return ResponseEntity.ok(productList);
	}

	@GetMapping("/{productRef}")
	public ResponseEntity<Product> getProduct(@PathVariable String productRef) {

		Product product = service.getProduct(productRef);

		if (product != null) {
			return ResponseEntity.ok(product);
		} else {
			return ResponseEntity.notFound().build();
		}

	}

}
