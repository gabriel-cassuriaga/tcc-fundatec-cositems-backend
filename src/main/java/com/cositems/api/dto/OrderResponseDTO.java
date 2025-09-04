package com.cositems.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import com.cositems.api.model.Order;
import com.cositems.api.model.Order.OrderStatus;

public record OrderResponseDTO(String id, String userId, LocalDateTime orderDate, OrderStatus status, BigDecimal total,
        List<Order.OrderItem> items) {

    public OrderResponseDTO(Order order) {
        this(order.getId(),
            order.getUserId(),
            order.getOrderDate(),
            order.getStatus(),
            order.getTotal(),
            order.getItems());
    }
    
}