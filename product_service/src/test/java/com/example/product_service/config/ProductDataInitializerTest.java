package com.example.product_service.config;

import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ProductDataInitializerTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	void createsDummyProducts() {
		assertThat(productRepository.count()).isGreaterThan(0);
	}
}
