package com.cositems.api.mapper;

import org.springframework.stereotype.Component;

import com.cositems.api.dto.ProductRequestDTO;
import com.cositems.api.dto.ProductResponseDTO;
import com.cositems.api.model.Product;

@Component
public class ProductMapper {
    public ProductResponseDTO toProductResponseDTO(Product product) {
        return new ProductResponseDTO(product.getId(), product.getSellerId(), product.getName(),
                product.getDescription(), product.getPrice(), product.getQuantity());
    }

    public Product toProduct(String loggedInSellerId, ProductRequestDTO productRequest) {
        return Product.builder()
                .name(productRequest.name())
                .sellerId(loggedInSellerId)
                .price(productRequest.price())
                .description(productRequest.description())
                .quantity(productRequest.quantity())
                .build();
    }

}
