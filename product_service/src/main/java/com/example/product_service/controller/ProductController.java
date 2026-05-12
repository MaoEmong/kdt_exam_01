package com.example.product_service.controller;

import com.example.product_service.dto.DecreaseStockRequest;
import com.example.product_service.service.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductController {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@PostMapping("/{productId}/decrease-stock")
	public ResponseEntity<Void> decreaseStock(
		@PathVariable Long productId,
		@RequestBody DecreaseStockRequest request
	) {
		productService.decreaseStock(productId, request.quantity());
		return ResponseEntity.noContent().build();
	}
}
