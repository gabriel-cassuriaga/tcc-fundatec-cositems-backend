package com.cositems.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.ShoppingCart;

public interface ShoppingCartRepository extends MongoRepository<ShoppingCart, String> {

}
