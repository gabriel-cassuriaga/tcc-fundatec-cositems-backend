package com.cositems.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.model.UserModel;
import com.cositems.api.service.ProductService;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private String getLoggedInUserId(Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        return user.getId();
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequest,
            Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);
        
        ProductResponseDTO productResponse = productService.createProduct(productRequest, loggedInSellerId);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ProductResponseDTO>> getAll() {
        List<ProductResponseDTO> products = productService.getAllProducts();
        return new ResponseEntity<>(products, HttpStatus.OK);

    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
        ProductResponseDTO productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id,
            @RequestBody ProductRequestDTO productRequest, Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);

        ProductResponseDTO productResponse = productService.updateProduct(id, productRequest, loggedInSellerId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<Void> deleteProduct(@PathVariable String productId, Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);

        productService.deleteProduct(productId, loggedInSellerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
