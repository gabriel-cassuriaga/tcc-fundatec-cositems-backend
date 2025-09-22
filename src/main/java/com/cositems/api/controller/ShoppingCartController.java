package com.cositems.api.controller;

import com.cositems.api.dto.CartItemRequestDTO;
import com.cositems.api.dto.ShoppingCartResponseDTO;
import com.cositems.api.model.UserModel;
import com.cositems.api.service.ShoppingCartService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    private UserModel getLoggedInUser(Authentication authentication) {
        return (UserModel) authentication.getPrincipal();
    }

    @GetMapping
    @PreAuthorize("hasRole('CUSTOMER')") // Apenas clientes têm carrinho de compras
    public ResponseEntity<ShoppingCartResponseDTO> getMyCart(Authentication authentication) {
        ShoppingCartResponseDTO cart = cartService.getCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ShoppingCartResponseDTO> addItemToMyCart(
            @RequestBody CartItemRequestDTO itemDto,
            Authentication authentication
    ) {
        ShoppingCartResponseDTO cart = cartService.addItemToCart(getLoggedInUser(authentication), itemDto);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ShoppingCartResponseDTO> removeItemFromMyCart(
            @PathVariable String productId,
            Authentication authentication
    ) {
        ShoppingCartResponseDTO cart = cartService.removeItemFromCart(getLoggedInUser(authentication), productId);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<ShoppingCartResponseDTO> clearMyCart(Authentication authentication) {
        ShoppingCartResponseDTO cart = cartService.clearCart(getLoggedInUser(authentication));
        return ResponseEntity.ok(cart);
    }
}