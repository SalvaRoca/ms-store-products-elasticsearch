package com.bassmania.msstoreproductselasticsearch.data;

import java.util.List;
import java.util.Optional;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import com.bassmania.msstoreproductselasticsearch.model.db.Product;

public interface ProductRepository extends ElasticsearchRepository<Product, String> {
	Optional<Product> findByProductRef(String id);

	List<Product> findAll();
}
