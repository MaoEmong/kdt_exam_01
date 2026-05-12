package com.example.product_service.service;

import com.example.product_service.domain.Product;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class ProductServiceTest {

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private ProductService productService;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		productRepository.save(new Product(1L, "Keyboard", 10));
	}

	@Test
	void decreaseStockReducesProductStock() {
		productService.decreaseStock(1L, 3);

		Product product = productRepository.findById(1L).orElseThrow();
		assertThat(product.getStock()).isEqualTo(7);
	}

	@Test
	void decreaseStockThrowsExceptionWhenStockIsNotEnough() {
		assertThatThrownBy(() -> productService.decreaseStock(1L, 11))
			.isInstanceOf(InsufficientStockException.class);

		Product product = productRepository.findById(1L).orElseThrow();
		assertThat(product.getStock()).isEqualTo(10);
	}
}
