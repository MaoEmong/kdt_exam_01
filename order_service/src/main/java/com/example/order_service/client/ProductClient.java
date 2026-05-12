package com.example.order_service.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class ProductClient {

	private final RestClient restClient;

	public ProductClient(RestClient.Builder restClientBuilder, @Value("${product-service.url}") String productServiceUrl) {
		this.restClient = restClientBuilder.baseUrl(productServiceUrl).build();
	}

	public void decreaseStock(Long productId, int quantity) {
		restClient.post()
			.uri("/products/{productId}/decrease-stock", productId)
			.body(new DecreaseStockRequest(quantity))
			.retrieve()
			.toBodilessEntity();
	}
}
