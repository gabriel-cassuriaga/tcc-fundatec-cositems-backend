package com.cositems.api.mapper;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.enums.OrderStatus;
import com.cositems.api.model.Order;
import com.cositems.api.model.OrderItem;
import com.cositems.api.model.Product;

@Component
public class OrderMapper {

    public OrderResponseDTO toOrderResponseDTO(Order order) {
        return new OrderResponseDTO(order.getId(), order.getUserId(), order.getOrderDate(), order.getStatus(),
                order.getTotal(), order.getTransactionId(), order.getItems());
    }

    public Order toOrder(String loggedInUserId, List<OrderItem> orderItems, String transactionId) {
        return Order.builder()
                .userId(loggedInUserId)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .total(Order.calculateTotal(orderItems))
                .transactionId(transactionId)
                .items(orderItems)
                .build();
    }

    public OrderItem toOrderItem(Product product, int quantity) {
        return OrderItem.builder()
                .productId(product.getId())
                .name(product.getName())
                .quantity(quantity)
                .price(product.getPrice())
                .build();
    }

}
