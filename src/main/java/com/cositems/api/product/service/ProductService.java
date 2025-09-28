package com.cositems.api.product.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cositems.api.exception.AuthorizationException;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.product.dto.ProductRequestDTO;
import com.cositems.api.product.dto.ProductResponseDTO;
import com.cositems.api.product.model.Product;
import com.cositems.api.product.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;

    public ProductResponseDTO createProduct(ProductRequestDTO productRequest, String loggedInSellerId) {

        if (productRepository.findBySellerIdAndName(loggedInSellerId, productRequest.name()).isPresent()) {
            throw new BusinessRuleException("Você já possui um produto cadastrado com este nome.");
        }

        Product product = Product.builder()
                .name(productRequest.name())
                .sellerId(loggedInSellerId)
                .price(productRequest.price())
                .description(productRequest.description())
                .quantity(productRequest.quantity())
                .build();

        Product savedProduct = productRepository.save(product);
        return new ProductResponseDTO(savedProduct);

    }

    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        return new ProductResponseDTO(product);

    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(ProductResponseDTO::new);
    }

    public void deleteProduct(String productId, String loggedInSellerId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + productId));

        if (!product.getSellerId().equals(loggedInSellerId)) {
            throw new AuthorizationException("Você não tem permissão para deletar este produto.");
        }

        productRepository.deleteById(productId);

    }

    public ProductResponseDTO updateProduct(String id, ProductRequestDTO productRequest, String loggedInSellerId) {

        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        if (!product.getSellerId().equals(loggedInSellerId)) {
            throw new AuthorizationException("Você não tem permissão para editar este produto.");
        }

        productRepository.findBySellerIdAndName(loggedInSellerId, productRequest.name())
                .ifPresent(existingProduct -> {
                    if (!existingProduct.getId().equals(id)) {
                        throw new BusinessRuleException("Você já possui outro produto cadastrado com este nome.");
                    }
                });

        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setDescription(productRequest.description());
        product.setQuantity(productRequest.quantity());

        Product savedProduct = productRepository.save(product);
        return new ProductResponseDTO(savedProduct);

    }
}