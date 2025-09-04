// Arquivo: src/main/java/com/cositems/api/service/OrderService.java
package com.cositems.api.service;

import com.cositems.api.dto.OrderRequestDTO;
import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.model.Order;
import com.cositems.api.model.Order.OrderStatus;
import com.cositems.api.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class OrderService {

    private final OrderRepository repository;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest) {
        List<Order.OrderItem> orderItems = orderRequest.items().stream()
                .map(itemDto -> {
                    Order.OrderItem item = new Order.OrderItem();
                    item.setProductId(itemDto.productId());
                    item.setName(itemDto.name());
                    item.setQuantity(itemDto.quantity());
                    item.setPrice(itemDto.price());
                    return item;
                }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(orderRequest.userId())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(orderItems)
                .total(total)
                .build();

        Order savedOrder = repository.save(order);

        return new OrderResponseDTO(savedOrder);

    }


    public List<OrderResponseDTO> getAllOrders() {
        return repository.findAll().stream()
                .map(OrderResponseDTO::new)
                .toList();

    }


    public OrderResponseDTO getOrderById(String id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido não encontrado com o id: " + id));
        return new OrderResponseDTO(order);

    }


    public OrderResponseDTO markAsPaid(String id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido não encontrado com o id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Apenas pedidos pendentes podem ser marcados como pagos. Status atual: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PAID);
        Order updatedOrder = repository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }


    public OrderResponseDTO markAsShipped(String id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido não encontrado com o id: " + id));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new IllegalStateException("Apenas pedidos pagos podem ser marcados como enviados. Status atual: " + order.getStatus());
        }

        order.setStatus(OrderStatus.SHIPPED);
        Order updatedOrder = repository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }


    public OrderResponseDTO cancelOrder(String id) {
        Order order = repository.findById(id).orElseThrow(() ->
                new RuntimeException("Pedido não encontrado com o id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Apenas pedidos pendentes podem ser cancelados. Status atual: " + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order updatedOrder = repository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }


}