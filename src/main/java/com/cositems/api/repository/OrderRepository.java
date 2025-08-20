package com.cositems.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    
}
