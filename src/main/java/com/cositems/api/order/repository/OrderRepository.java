package com.cositems.api.order.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.order.model.Order;

public interface OrderRepository extends MongoRepository<Order, String> {
    List<Order> findByUserId(String userId);
}
