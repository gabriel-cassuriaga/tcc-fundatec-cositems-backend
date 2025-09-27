package com.cositems.api.cart.dto;

import java.util.List;

import com.cositems.api.cart.model.Cart;
import com.cositems.api.cart.model.CartItem;

public record CartResponseDTO(String id, String customerId, List<CartItem> cartItems) {

    public CartResponseDTO(Cart cart) {
        this(cart.getId(), cart.getCustomerId(), cart.getCartItems());
    }
}