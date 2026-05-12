package com.example.order_service.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "orders")
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private Long productId;
	private int quantity;

	@Enumerated(EnumType.STRING)
	private OrderStatus status;

	protected Order() {
	}

	public Order(Long productId, int quantity) {
		this(productId, quantity, OrderStatus.SUCCESS);
	}

	public Order(Long productId, int quantity, OrderStatus status) {
		this.productId = productId;
		this.quantity = quantity;
		this.status = status;
	}

	public Long getId() {
		return id;
	}

	public Long getProductId() {
		return productId;
	}

	public int getQuantity() {
		return quantity;
	}

	public OrderStatus getStatus() {
		return status;
	}
}
