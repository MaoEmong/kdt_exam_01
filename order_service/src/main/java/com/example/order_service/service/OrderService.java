package com.example.order_service.service;

import com.example.order_service.client.ProductClient;
import com.example.order_service.domain.Order;
import com.example.order_service.domain.OrderStatus;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.repository.OrderRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

@Service
public class OrderService {

	private final OrderRepository orderRepository;
	private final ProductClient productClient;

	public OrderService(OrderRepository orderRepository, ProductClient productClient) {
		this.orderRepository = orderRepository;
		this.productClient = productClient;
	}

	@Transactional
	public Order purchase(OrderRequest request) {
		try {
			productClient.decreaseStock(request.productId(), request.quantity());
			return orderRepository.save(new Order(request.productId(), request.quantity(), OrderStatus.SUCCESS));
		} catch (RestClientException exception) {
			return orderRepository.save(new Order(request.productId(), request.quantity(), OrderStatus.FAILED));
		}
	}
}
