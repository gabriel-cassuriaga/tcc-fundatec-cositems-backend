package com.cositems.api.product.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.product.model.Product;

public interface ProductRepository extends MongoRepository<Product, String> {
    Optional<Product> findByName(String name);

    Optional<Product> findBySellerIdAndName(String sellerId, String name);
}
