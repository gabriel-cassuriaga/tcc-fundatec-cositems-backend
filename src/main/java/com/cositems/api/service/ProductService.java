package com.cositems.api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.exception.ValidationException;
import com.cositems.api.model.ProductModel;
import com.cositems.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;

    private void validateProductRequest(ProductRequestDTO productRequest) {
        if (productRequest.name() == null || productRequest.name().isBlank()) {
            throw new ValidationException("O nome do produto não pode ser vazio.");
        }
        if (productRequest.price() == null || productRequest.price().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("O preço do produto deve ser um valor positivo.");
        }
    }

    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        validateProductRequest(productRequest);

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
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));
        return new ProductResponseDTO(product);
    }

    public List<ProductResponseDTO> getAllProducts() {
        return repository.findAll().stream()
                .map(ProductResponseDTO::new)
                .toList();
    }

    public void deleteProduct(String id) {
        ProductModel product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        repository.deleteById(product.getId());
    }

    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest) {
        ProductModel product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        validateProductRequest(productRequest);

        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setDescription(productRequest.description());

        ProductModel savedProduct = repository.save(product);
        return new ProductResponseDTO(savedProduct);
    }
}