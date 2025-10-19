package com.cositems.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.exception.AuthorizationException;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.mapper.ProductMapper;
import com.cositems.api.model.Product;
import com.cositems.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor

public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public ProductResponseDTO createProduct(ProductRequestDTO productRequest, String loggedInSellerId) {

        if (productRepository.findBySellerIdAndName(loggedInSellerId, productRequest.name()).isPresent()) {
            throw new BusinessRuleException("Você já possui um produto cadastrado com este nome.");
        }

        Product product = productMapper.toProduct(loggedInSellerId, productRequest);
        Product savedProduct = productRepository.save(product);

        return productMapper.toProductResponseDTO(savedProduct);
    }

    public ProductResponseDTO getProductById(String id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Produto não encontrado com o id: " + id));

        return productMapper.toProductResponseDTO(product);
    }

    public Page<ProductResponseDTO> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(productMapper::toProductResponseDTO);
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

        applyUpdatesToProduct(product, productRequest);

        Product savedProduct = productRepository.save(product);
        return productMapper.toProductResponseDTO(savedProduct);
    }

    private void applyUpdatesToProduct(Product product, ProductRequestDTO productRequest) {
        product.setName(productRequest.name());
        product.setPrice(productRequest.price());
        product.setDescription(productRequest.description());
        product.setQuantity(productRequest.quantity());
    }
}