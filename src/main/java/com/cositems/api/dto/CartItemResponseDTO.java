package com.cositems.api.dto;

import java.math.BigDecimal;

import com.cositems.api.model.ShoppingCart;

public record CartItemResponseDTO(String productId, String name, int quantity, BigDecimal price) {
    public CartItemResponseDTO(ShoppingCart.CartItem cartItem) {
        this(cartItem.getProductId(),
                cartItem.getName(),
                cartItem.getQuantity(),
                cartItem.getPrice());
    }
}