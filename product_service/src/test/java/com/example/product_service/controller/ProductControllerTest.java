package com.example.product_service.controller;

import com.example.product_service.domain.Product;
import com.example.product_service.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ProductRepository productRepository;

	@BeforeEach
	void setUp() {
		productRepository.deleteAll();
		productRepository.save(new Product(1L, "Keyboard", 10));
	}

	@Test
	void decreaseStockApiReducesProductStock() throws Exception {
		mockMvc.perform(post("/products/1/decrease-stock")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"quantity\":3}"))
			.andExpect(status().isNoContent());

		Product product = productRepository.findById(1L).orElseThrow();
		assertThat(product.getStock()).isEqualTo(7);
	}

	@Test
	void decreaseStockApiReturnsBadRequestWhenStockIsNotEnough() throws Exception {
		mockMvc.perform(post("/products/1/decrease-stock")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"quantity\":11}"))
			.andExpect(status().isBadRequest());

		Product product = productRepository.findById(1L).orElseThrow();
		assertThat(product.getStock()).isEqualTo(10);
	}
}
