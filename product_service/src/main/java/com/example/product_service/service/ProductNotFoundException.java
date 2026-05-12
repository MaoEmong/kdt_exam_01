package com.example.product_service.service;

public class ProductNotFoundException extends RuntimeException {

	public ProductNotFoundException(Long productId) {
		super("Product not found. productId=" + productId);
	}
}
