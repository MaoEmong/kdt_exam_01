package com.example.product_service.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

	@Test
	void productHasIdNameAndStock() {
		Product product = new Product(1L, "Keyboard", 10);

		assertThat(product.getId()).isEqualTo(1L);
		assertThat(product.getName()).isEqualTo("Keyboard");
		assertThat(product.getStock()).isEqualTo(10);
	}
}
