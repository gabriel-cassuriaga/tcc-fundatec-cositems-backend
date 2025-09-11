package com.cositems.api.dto;

import java.math.BigDecimal;

import com.cositems.api.model.ProductModel;

public record ProductResponseDTO(String id, String sellerId, String name, String description, BigDecimal price) {
    
    public ProductResponseDTO(ProductModel product) {
        this(product.getId(), product.getSellerId(), product.getName(), product.getDescription(), product.getPrice());
    }


}
