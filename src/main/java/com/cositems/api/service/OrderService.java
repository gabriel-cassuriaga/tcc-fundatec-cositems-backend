package com.cositems.api.service;

import com.cositems.api.dto.OrderRequestDTO;
import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.exception.AuthorizationException;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.exception.ValidationException;
import com.cositems.api.model.Order;
import com.cositems.api.model.Order.OrderStatus;
import com.cositems.api.model.ProductModel;
import com.cositems.api.model.UserModel;
import com.cositems.api.repository.OrderRepository;
import com.cositems.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest, String loggedInUserId) {

        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new ValidationException("O pedido deve conter pelo menos um item.");
        }

        List<Order.OrderItem> orderItems = orderRequest.items().stream()
                .map(itemDto -> {

                    ProductModel product = productRepository.findById(itemDto.productId())
                            .orElseThrow(() -> new ResourceNotFoundException(
                                    "Produto com id " + itemDto.productId() + " não encontrado."));

                    if (product.getQuantity() < itemDto.quantity()) {
                        throw new BusinessRuleException("Estoque insuficiente para o produto: " + product.getName());
                    }

                    product.setQuantity(product.getQuantity() - itemDto.quantity());
                    productRepository.save(product);

                    return Order.OrderItem.builder()
                            .productId(product.getId())
                            .name(product.getName())
                            .quantity(itemDto.quantity())
                            .price(product.getPrice())
                            .build();

                }).collect(Collectors.toList());

        Order order = Order.builder()
                .userId(loggedInUserId)
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(orderItems)
                .total(Order.calculateTotal(orderItems))
                .build();

        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDTO(savedOrder);

    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream().map(OrderResponseDTO::new).toList();
    }

    public OrderResponseDTO getOrderById(String id, UserModel loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        validateOwnershipOrAdmin(order, loggedInUser);

        return new OrderResponseDTO(order);

    }

    @Transactional
    public OrderResponseDTO markAsPaid(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        order.markAsPaid();

        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }

    @Transactional
    public OrderResponseDTO markAsShipped(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        order.markAsShipped();

        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }

    @Transactional
    public OrderResponseDTO cancelOrder(String id, UserModel loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        validateOwnershipOrAdmin(order, loggedInUser);

        order.cancel();
        Order updatedOrder = orderRepository.save(order);

        return new OrderResponseDTO(updatedOrder);

    }

    private void validateOwnershipOrAdmin(Order order, UserModel user) {
        boolean isOwner = order.getUserId().equals(user.getId());
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new AuthorizationException("Você não tem permissão para acessar este pedido.");
        }
    }

    public List<OrderResponseDTO> getUserOrders(String loggedInCustomerId) {
        List<Order> orders = orderRepository.findByUserId(loggedInCustomerId);

        return orders.stream()
                    .map(OrderResponseDTO::new)
                    .toList();
    }

}