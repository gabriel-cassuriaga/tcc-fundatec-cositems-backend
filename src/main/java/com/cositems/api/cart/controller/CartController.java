package com.cositems.api.cart.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.cart.dto.CartItemRequestDTO;
import com.cositems.api.cart.dto.CartResponseDTO;
import com.cositems.api.cart.service.CartService;
import com.cositems.api.user.model.User;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    private User getLoggedInUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> getMyCart(Authentication authentication) {
        CartResponseDTO cart = cartService.getCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> addItemToMyCart(
            @RequestBody CartItemRequestDTO itemDto,
            Authentication authentication) {
        CartResponseDTO cart = cartService.addItemToCart(getLoggedInUser(authentication), itemDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> removeItemFromMyCart(
            @PathVariable String productId,
            Authentication authentication) {
        CartResponseDTO cart = cartService.removeItemFromCart(getLoggedInUser(authentication), productId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CartResponseDTO> clearMyCart(Authentication authentication) {
        CartResponseDTO cart = cartService.clearCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }
}