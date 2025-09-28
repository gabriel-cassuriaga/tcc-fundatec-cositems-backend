package com.cositems.api.product.dto;

import java.math.BigDecimal;

import com.cositems.api.product.model.Product;

public record ProductResponseDTO(String id, String sellerId, String name, String description, BigDecimal price, int quantity) {

    public ProductResponseDTO(Product product) {
        this(product.getId(), product.getSellerId(), product.getName(), product.getDescription(), product.getPrice(), product.getQuantity());
    }
}
