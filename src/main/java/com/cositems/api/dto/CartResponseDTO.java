package com.cositems.api.dto;

import java.util.List;

import com.cositems.api.model.Cart;
import com.cositems.api.model.CartItem;

public record CartResponseDTO(String id, String customerId, List<CartItem> cartItems) {

    public CartResponseDTO(Cart cart) {
        this(cart.getId(), cart.getCustomerId(), cart.getCartItems());
    }
}