package com.cositems.api.mapper;

import org.springframework.stereotype.Component;

import com.cositems.api.dto.CartResponseDTO;
import com.cositems.api.model.Cart;
import com.cositems.api.model.CartItem;
import com.cositems.api.model.Product;

@Component
public class CartMapper {
    public CartResponseDTO toCartResponseDTO(Cart cart) {
        return new CartResponseDTO(cart.getId(), cart.getCustomerId(), cart.getCartItems());
    }

    public Cart toCart(String customerId) {
        return Cart.builder()
                .customerId(customerId)
                .build();
    }

    public CartItem toCartItem(Product product, int quantity) {
        return CartItem.builder()
                .productId(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .quantity(quantity)
                .build();
    }
}
