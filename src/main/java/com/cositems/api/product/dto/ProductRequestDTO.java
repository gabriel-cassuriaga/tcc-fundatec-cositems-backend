package com.cositems.api.product.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record ProductRequestDTO(
        @NotBlank(message = "O nome do produto não pode ser vazio.")
        @Size(min = 3, max = 100, message = "O nome do produto deve ter entre 3 e 100 caracteres.")
        String name,

        @NotNull(message = "O preço não pode ser nulo.")
        @Positive(message = "O preço do produto deve ser um valor positivo.")
        BigDecimal price,

        @Size(max = 500, message = "A descrição não pode exceder 500 caracteres.")
        String description,
        
        int quantity) {
}