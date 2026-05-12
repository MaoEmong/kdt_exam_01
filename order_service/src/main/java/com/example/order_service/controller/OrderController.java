package com.example.order_service.controller;

import com.example.order_service.domain.Order;
import com.example.order_service.dto.OrderRequest;
import com.example.order_service.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
public class OrderController {

	private final OrderService orderService;

	public OrderController(OrderService orderService) {
		this.orderService = orderService;
	}

	@PostMapping
	public ResponseEntity<Order> purchase(@RequestBody OrderRequest request) {
		Order order = orderService.purchase(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(order);
	}
}
