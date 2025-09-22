package com.cositems.api.service;

import com.cositems.api.dto.CartItemDTO;
import com.cositems.api.dto.ShoppingCartResponseDTO;
import com.cositems.api.model.ShoppingCart;
import com.cositems.api.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;

    public ShoppingCartResponseDTO getCartByCustomerId(String customerId) {
        ShoppingCart cart = findOrCreateCartByCustomerId(customerId);
        return new ShoppingCartResponseDTO(cart);
    }

    public ShoppingCartResponseDTO addItemToCart(String customerId, CartItemDTO itemDto) {
        ShoppingCart cart = findOrCreateCartByCustomerId(customerId);

        Optional<ShoppingCart.CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(itemDto.productId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(itemDto.quantity());
        } else {
            ShoppingCart.CartItem newItem = new ShoppingCart().new CartItem();
            newItem.setProductId(itemDto.productId());
            newItem.setQuantity(itemDto.quantity());
            cart.getCartItems().add(newItem);
        }

        ShoppingCart updatedCart = cartRepository.save(cart);
        return new ShoppingCartResponseDTO(updatedCart);
    }

    public ShoppingCartResponseDTO removeItemFromCart(String customerId, String productId) {
        ShoppingCart cart = findOrCreateCartByCustomerId(customerId);

        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));

        ShoppingCart updatedCart = cartRepository.save(cart);
        return new ShoppingCartResponseDTO(updatedCart);
    }

    public ShoppingCartResponseDTO clearCart(String customerId) {
        ShoppingCart cart = findOrCreateCartByCustomerId(customerId);
        cart.getCartItems().clear();

        ShoppingCart updatedCart = cartRepository.save(cart);
        return new ShoppingCartResponseDTO(updatedCart);
    }

    private ShoppingCart findOrCreateCartByCustomerId(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    ShoppingCart newCart = ShoppingCart.builder()
                            .customerId(customerId)
                            .build();
                    return cartRepository.save(newCart);
                });
    }
}