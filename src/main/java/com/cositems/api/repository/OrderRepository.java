package com.cositems.api.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);

}
