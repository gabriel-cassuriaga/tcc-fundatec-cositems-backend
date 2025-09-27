package com.cositems.api.order.dto;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {
    public record OrderItemRequestDTO(String productId, int quantity) {
    }
}