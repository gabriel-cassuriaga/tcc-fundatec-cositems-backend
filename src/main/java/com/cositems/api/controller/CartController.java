package com.cositems.api.controller;

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

import com.cositems.api.dto.CartItemRequestDTO;
import com.cositems.api.dto.CartResponseDTO;
import com.cositems.api.model.User;
import com.cositems.api.service.CartService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    private final CartService cartService;

    private User getLoggedInUser(Authentication authentication) {
        return (User) authentication.getPrincipal();
    }

    @GetMapping
    public ResponseEntity<CartResponseDTO> getMyCart(Authentication authentication) {
        CartResponseDTO cart = cartService.getCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDTO> addItemToMyCart(@RequestBody @Valid CartItemRequestDTO itemDto,
            Authentication authentication) {
        CartResponseDTO cart = cartService.addItemToCart(getLoggedInUser(authentication), itemDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{productId}")
    public ResponseEntity<CartResponseDTO> removeItemFromMyCart(@PathVariable String productId,
            Authentication authentication) {
        CartResponseDTO cart = cartService.removeItemFromCart(getLoggedInUser(authentication), productId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    public ResponseEntity<CartResponseDTO> clearMyCart(Authentication authentication) {
        CartResponseDTO cart = cartService.clearCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }
}