package com.cositems.api.order.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cositems.api.order.enums.OrderStatus;
import com.cositems.api.order.model.Order;
import com.cositems.api.order.model.OrderItem;

public record OrderResponseDTO(String id, String userId, LocalDateTime orderDate, OrderStatus status, BigDecimal total,
        List<OrderItem> items) {

    public OrderResponseDTO(Order order) {
        this(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotal(),
                order.getItems());
    }
}