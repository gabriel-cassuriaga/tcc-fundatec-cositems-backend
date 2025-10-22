package com.cositems.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.cositems.api.enums.OrderStatus;
import com.cositems.api.model.Order;
import com.cositems.api.model.OrderItem;

public record OrderResponseDTO(String id, String userId, LocalDateTime orderDate, OrderStatus status,
        BigDecimal total,
        String transactionId,
        List<OrderItem> items) {

    public OrderResponseDTO(Order order) {
        this(
                order.getId(),
                order.getUserId(),
                order.getOrderDate(),
                order.getStatus(),
                order.getTotal(),
                order.getTransactionId(),
                order.getItems());
    }
}