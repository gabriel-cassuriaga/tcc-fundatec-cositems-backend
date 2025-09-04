package com.cositems.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record OrderRequestDTO(String userId, List<OrderItemRequestDTO> items) {
            
    public record OrderItemRequestDTO(String productId, String name, int quantity, BigDecimal price) {
    }

}