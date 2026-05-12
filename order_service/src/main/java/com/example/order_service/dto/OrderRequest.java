package com.example.order_service.dto;

public record OrderRequest(Long productId, int quantity) {
}
