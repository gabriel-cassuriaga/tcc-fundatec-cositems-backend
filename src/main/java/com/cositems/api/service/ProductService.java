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
        ProductModel product = ProductModel.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .description(productRequest.description())
                .build();

        ProductModel savedProduct = repository.save(product);
        return new ProductResponseDTO(savedProduct);

    }

    
    public ProductResponseDTO getProductById(String id) {
        ProductModel product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return new ProductResponseDTO(product);

    }


    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream()
                .map(ProductResponseDTO::new)
                .toList();

    }


    public void deleteProduct(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Produto não encontrado com o id: " + id);
        }
        repository.deleteById(id);

    }


    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest) {
        ProductModel product = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setDescription(productRequest.description());

        ProductModel savedProduct = repository.save(product);
        return new ProductResponseDTO(savedProduct);

    }

}
