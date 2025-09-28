package com.cositems.api.product.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.product.dto.ProductRequestDTO;
import com.cositems.api.product.dto.ProductResponseDTO;
import com.cositems.api.product.service.ProductService;
import com.cositems.api.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    private String getLoggedInUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    @PostMapping
    @PreAuthorize("hasRole('SELLER')")
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody @Valid ProductRequestDTO productRequest,
            Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);

        ProductResponseDTO productResponse = productService.createProduct(productRequest, loggedInSellerId);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ProductResponseDTO>> getAll(Pageable pageable) {
        Page<ProductResponseDTO> productsPage = productService.getAllProducts(pageable);
        return new ResponseEntity<>(productsPage, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable String id) {
        ProductResponseDTO productResponse = productService.getProductById(id);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') and @productService.isProductOwner(#id, authentication.principal.id)")
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id,
            @RequestBody @Valid ProductRequestDTO productRequest, Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);

        ProductResponseDTO productResponse = productService.updateProduct(id, productRequest, loggedInSellerId);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SELLER') and @productService.isProductOwner(#id, authentication.principal.id)")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id, Authentication authentication) {
        String loggedInSellerId = getLoggedInUserId(authentication);

        productService.deleteProduct(id, loggedInSellerId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}