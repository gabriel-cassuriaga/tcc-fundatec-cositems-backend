package com.cositems.api.dto;

import com.cositems.api.enums.PaymentMethodType;

import jakarta.validation.constraints.NotNull;

public record PaymentRequestDTO(
        @NotNull(message = "O método de pagamento é obrigatório.") PaymentMethodType method,
        String paymentToken,
        Integer installments) {

}