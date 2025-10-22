package com.cositems.api.dto;

public record PaymentResultDTO(String transactionId, String barcode, String qrCodeUrl) {
}