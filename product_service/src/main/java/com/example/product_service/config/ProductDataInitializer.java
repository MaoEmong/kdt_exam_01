package com.example.product_service.config;

import com.example.product_service.domain.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ProductDataInitializer implements CommandLineRunner {

	private final ProductRepository productRepository;

	public ProductDataInitializer(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public void run(String... args) {
		if (productRepository.count() > 0) {
			return;
		}

		productRepository.save(new Product(1L, "Keyboard", 10));
		productRepository.save(new Product(2L, "Mouse", 20));
		productRepository.save(new Product(3L, "Monitor", 5));
	}
}
