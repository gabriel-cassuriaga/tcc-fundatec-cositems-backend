package com.cositems.api.dto;

import java.util.List;
import com.cositems.api.model.ShoppingCart;

public record ShoppingCartResponseDTO(String id, String customerId, List<ShoppingCart.CartItem> cartItems) {
    
    public ShoppingCartResponseDTO(ShoppingCart cart) {
        this(cart.getId(), cart.getCustomerId(), cart.getCartItems());
    }
}