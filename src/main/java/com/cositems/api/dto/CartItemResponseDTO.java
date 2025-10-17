package com.cositems.api.dto;

import java.math.BigDecimal;

import com.cositems.api.model.CartItem;

public record CartItemResponseDTO(String productId, String name, int quantity, BigDecimal price) {
    public CartItemResponseDTO(CartItem cartItem) {
        this(cartItem.getProductId(),
                cartItem.getName(),
                cartItem.getQuantity(),
                cartItem.getPrice());
    }
}