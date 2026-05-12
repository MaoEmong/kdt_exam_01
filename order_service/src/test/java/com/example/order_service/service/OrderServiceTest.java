package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderStatus;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.repository.OrderRepository;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpMethod;
import org.springframework.test.web.client.MockRestServiceServer;
import org.springframework.web.client.RestClient;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withBadRequest;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withNoContent;

class OrderServiceTest {

	@Test
	void purchaseDecreasesProductStockAndSavesOrder() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
		OrderRepository orderRepository = mock(OrderRepository.class);
		ProductClient productClient = new ProductClient(restClientBuilder, "http://localhost:8082");
		OrderService orderService = new OrderService(orderRepository, productClient);

		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
		server.expect(requestTo("http://localhost:8082/products/1/decrease-stock"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(content().json("{\"quantity\":3}"))
			.andRespond(withNoContent());

		Order order = orderService.purchase(new OrderRequest(1L, 3));

		assertThat(order.getProductId()).isEqualTo(1L);
		assertThat(order.getQuantity()).isEqualTo(3);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.SUCCESS);
		server.verify();
	}

	@Test
	void purchaseSavesFailedOrderWhenProductStockDecreaseFails() {
		RestClient.Builder restClientBuilder = RestClient.builder();
		MockRestServiceServer server = MockRestServiceServer.bindTo(restClientBuilder).build();
		OrderRepository orderRepository = mock(OrderRepository.class);
		ProductClient productClient = new ProductClient(restClientBuilder, "http://localhost:8082");
		OrderService orderService = new OrderService(orderRepository, productClient);

		when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
		server.expect(requestTo("http://localhost:8082/products/1/decrease-stock"))
			.andExpect(method(HttpMethod.POST))
			.andExpect(content().json("{\"quantity\":30}"))
			.andRespond(withBadRequest());

		Order order = orderService.purchase(new OrderRequest(1L, 30));

		assertThat(order.getProductId()).isEqualTo(1L);
		assertThat(order.getQuantity()).isEqualTo(30);
		assertThat(order.getStatus()).isEqualTo(OrderStatus.FAILED);
		server.verify();
	}
}
