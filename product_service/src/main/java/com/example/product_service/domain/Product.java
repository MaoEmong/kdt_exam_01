package com.example.product_service.domain;

import com.example.product_service.service.InsufficientStockException;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class Product {

	@Id
	private Long id;
	private String name;
	private int stock;

	protected Product() {
	}

	public Product(Long id, String name, int stock) {
		this.id = id;
		this.name = name;
		this.stock = stock;
	}

	public void decreaseStock(int quantity) {
		if (stock < quantity) {
			throw new InsufficientStockException("Stock is not enough.");
		}
		stock -= quantity;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getStock() {
		return stock;
	}
}
