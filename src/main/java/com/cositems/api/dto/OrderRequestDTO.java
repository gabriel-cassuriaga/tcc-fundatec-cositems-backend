package com.cositems.api.dto;

import java.util.List;

public record OrderRequestDTO(List<OrderItemRequestDTO> items) {
    public record OrderItemRequestDTO(String productId, int quantity) {
    }

}