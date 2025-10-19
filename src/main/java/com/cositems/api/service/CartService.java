package com.cositems.api.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.CartItemRequestDTO;
import com.cositems.api.dto.CartResponseDTO;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.mapper.CartMapper;
import com.cositems.api.model.Cart;
import com.cositems.api.model.CartItem;
import com.cositems.api.model.Product;
import com.cositems.api.model.User;
import com.cositems.api.repository.CartRepository;
import com.cositems.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    public CartResponseDTO getCart(User loggedInUser) {
        Cart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
        return cartMapper.toCartResponseDTO(cart);
    }

    public CartResponseDTO addItemToCart(User loggedInUser, CartItemRequestDTO itemDto) {

        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto com id " + itemDto.productId() + " não encontrado."));

        Cart cart = findOrCreateCartByCustomerId(loggedInUser.getId());

        Optional<CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(itemDto.productId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(itemDto.quantity());
        } else {
            CartItem newItem = cartMapper.toCartItem(product, itemDto.quantity());

            cart.getCartItems().add(newItem);
        }

        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDTO(updatedCart);
    }

    public CartResponseDTO removeItemFromCart(User loggedInUser, String productId) {
        Cart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDTO(updatedCart);
    }

    public CartResponseDTO clearCart(User loggedInUser) {
        Cart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
        cart.getCartItems().clear();
        Cart updatedCart = cartRepository.save(cart);
        return cartMapper.toCartResponseDTO(updatedCart);
    }

    private Cart findOrCreateCartByCustomerId(String customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Cart newCart = cartMapper.toCart(customerId);
                    return cartRepository.save(newCart);
                });
    }

}