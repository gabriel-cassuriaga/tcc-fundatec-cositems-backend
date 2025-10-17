package com.cositems.api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;
import java.util.List;

public record OrderRequestDTO(
        @NotEmpty(message = "O pedido deve conter pelo menos um item.")
        @Valid List<OrderItemRequestDTO> items) {
            
    public record OrderItemRequestDTO(
            @NotBlank(message = "O ID do produto não pode ser vazio.") String productId,
            @Positive(message = "A quantidade deve ser um valor positivo.") int quantity) {
    }
}