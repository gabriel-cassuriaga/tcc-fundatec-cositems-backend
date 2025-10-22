package com.cositems.api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.cositems.api.enums.OrderStatus;
import com.cositems.api.exception.BusinessRuleException;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
    private LocalDateTime orderDate;
    private OrderStatus status;
    private BigDecimal total;
    private String transactionId;

    private List<OrderItem> items;

    public static BigDecimal calculateTotal(List<OrderItem> orderItems) {
        return orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public void markAsPaid() {
        if (status != OrderStatus.PENDING) {
            throw new BusinessRuleException(
                    "Apenas pedidos pendentes podem ser marcados como pagos. Status atual: " + status);
        }
        this.status = OrderStatus.PAID;
    }

    public void markAsShipped() {
        if (status != OrderStatus.PAID) {
            throw new BusinessRuleException(
                    "Apenas pedidos pagos podem ser marcados como enviados. Status atual: " + status);
        }
        this.status = OrderStatus.SHIPPED;
    }

    public void cancel() {
        if (status != OrderStatus.PENDING) {
            throw new BusinessRuleException("Apenas pedidos pendentes podem ser cancelados. Status atual: " + status);
        }
        this.status = OrderStatus.CANCELLED;
    }

}