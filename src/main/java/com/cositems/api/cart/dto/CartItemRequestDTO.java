package com.cositems.api.cart.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

public record CartItemRequestDTO(
    @NotBlank(message = "O ID do produto não pode ser vazio.")
    String productId,

    @Positive(message = "A quantidade do item deve ser um valor positivo.")
    int quantity) {
}