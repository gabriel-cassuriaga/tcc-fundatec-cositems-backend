package com.cositems.api.model;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "orders")
public class Order {
    @Id
    private String id;
    private String userId;
    private LocalDateTime data;
    private OrderStatus status;
    private double total;

    private List<OrderItem> itens;

    @Data
    public static class OrderItem {
        private String produtoId;
        private String nome;
        private int quantidade;
        private double precoUnitario;
    }
}