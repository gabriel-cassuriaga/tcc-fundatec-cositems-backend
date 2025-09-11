package com.cositems.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.ProductModel;

public interface ProductRepository extends MongoRepository<ProductModel, String> {
    Optional<ProductModel> findByName(String name);
    Optional<ProductModel> findBySellerIdAndName(String sellerId, String name);

}
