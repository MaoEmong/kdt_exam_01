package com.example.order_service.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OrderTest {

	@Test
	void orderHasProductIdAndQuantity() {
		Order order = new Order(1L, 3);

		assertThat(order.getProductId()).isEqualTo(1L);
		assertThat(order.getQuantity()).isEqualTo(3);
	}
}
