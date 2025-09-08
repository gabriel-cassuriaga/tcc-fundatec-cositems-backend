package com.cositems.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.service.ProductService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @PostMapping
    public ResponseEntity<ProductResponseDTO> createProduct(@RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO productResponse = productService.createProduct(productRequest);
        
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
    public ResponseEntity<ProductResponseDTO> updateProduct(@PathVariable String id, @RequestBody ProductRequestDTO productRequest) {
        ProductResponseDTO productResponse = productService.updateProduct(id, productRequest);
        return new ResponseEntity<>(productResponse, HttpStatus.OK);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
