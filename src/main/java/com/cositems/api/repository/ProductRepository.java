package com.cositems.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.ProductModel;

public interface ProductRepository extends MongoRepository<ProductModel, String> {

}
