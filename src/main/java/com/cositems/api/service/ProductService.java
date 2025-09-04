package com.cositems.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.model.ProductModel;
import com.cositems.api.repository.ProductRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        ProductModel product =  ProductModel.builder()
            .name(productRequest.name())
            .price(productRequest.price())
            .description(productRequest.description())
            .build();

        repository.save(product);

        ProductResponseDTO productResponse = new ProductResponseDTO(product);
        return productResponse;

    }


    public ProductResponseDTO getProductById(String id) {
        ProductModel product = repository.findById(id).orElseThrow(() -> 
            new RuntimeException("Produto não encontrado"));

        ProductResponseDTO productResponse = new ProductResponseDTO(product);
        return productResponse;

    }


    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream()
            .map(ProductResponseDTO::new)
            .toList();

    }


    public void deleteProduct(String id) {
        ProductModel product = repository.findById(id).orElseThrow(() -> 
            new RuntimeException("Produto não encontrado"));
        
        repository.delete(product);

    }


    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest) {
        ProductModel product = repository.findById(id).orElseThrow(() ->
            new RuntimeException("Produto não encontrado"));

        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setDescription(productRequest.description());

        repository.save(product);

        return new ProductResponseDTO(product);

    }


}
