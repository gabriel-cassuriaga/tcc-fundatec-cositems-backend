package com.cositems.api.service;

import com.cositems.api.dto.CartItemRequestDTO;
import com.cositems.api.dto.ShoppingCartResponseDTO;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.exception.ValidationException;
import com.cositems.api.model.ProductModel;
import com.cositems.api.model.ShoppingCart;
import com.cositems.api.model.UserModel;
import com.cositems.api.repository.ProductRepository;
import com.cositems.api.repository.ShoppingCartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ShoppingCartService {

    private final ShoppingCartRepository cartRepository;
    private final ProductRepository productRepository;

    public ShoppingCartResponseDTO getCart(UserModel loggedInUser) {
        ShoppingCart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
        return new ShoppingCartResponseDTO(cart);
    }

    public ShoppingCartResponseDTO addItemToCart(UserModel loggedInUser, CartItemRequestDTO itemDto) {
        if (itemDto.quantity() <= 0) {
            throw new ValidationException("A quantidade do item deve ser um valor positivo.");
        }

        ProductModel product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto com id " + itemDto.productId() + " não encontrado."));

        ShoppingCart cart = findOrCreateCartByCustomerId(loggedInUser.getId());

        Optional<ShoppingCart.CartItem> existingItem = cart.getCartItems().stream()
                .filter(item -> item.getProductId().equals(itemDto.productId()))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(itemDto.quantity());
        } else {
            ShoppingCart.CartItem newItem = ShoppingCart.CartItem.builder()
                    .productId(product.getId())
                    .name(product.getName())
                    .price(product.getPrice())
                    .quantity(itemDto.quantity())
                    .build();

            cart.getCartItems().add(newItem);
        }

        ShoppingCart updatedCart = cartRepository.save(cart);
        return new ShoppingCartResponseDTO(updatedCart);
    }

    public ShoppingCartResponseDTO removeItemFromCart(UserModel loggedInUser, String productId) {
        ShoppingCart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
        cart.getCartItems().removeIf(item -> item.getProductId().equals(productId));
        ShoppingCart updatedCart = cartRepository.save(cart);
        return new ShoppingCartResponseDTO(updatedCart);
    }

    public ShoppingCartResponseDTO clearCart(UserModel loggedInUser) {
        ShoppingCart cart = findOrCreateCartByCustomerId(loggedInUser.getId());
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