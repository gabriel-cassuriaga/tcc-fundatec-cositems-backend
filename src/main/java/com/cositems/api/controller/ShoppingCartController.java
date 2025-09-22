package com.cositems.api.controller;

import com.cositems.api.dto.CartItemDTO;
import com.cositems.api.dto.ShoppingCartResponseDTO;
import com.cositems.api.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @GetMapping("/{customerId}")
    public ResponseEntity<ShoppingCartResponseDTO> getMyCart(@PathVariable String customerId) {
        ShoppingCartResponseDTO cart = cartService.getCartByCustomerId(customerId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PostMapping("/{customerId}/items")
    public ResponseEntity<ShoppingCartResponseDTO> addItemToMyCart(@PathVariable String customerId,
            @RequestBody CartItemDTO itemDto) {
        ShoppingCartResponseDTO cart = cartService.addItemToCart(customerId, itemDto);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}/items/{productId}")
    public ResponseEntity<ShoppingCartResponseDTO> removeItemFromMyCart(@PathVariable String customerId,
            @PathVariable String productId) {
        ShoppingCartResponseDTO cart = cartService.removeItemFromCart(customerId, productId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @DeleteMapping("/{customerId}/items")
    public ResponseEntity<ShoppingCartResponseDTO> clearMyCart(@PathVariable String customerId) {
        ShoppingCartResponseDTO cart = cartService.clearCart(customerId);
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }
}