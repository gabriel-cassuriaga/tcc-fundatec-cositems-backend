package com.cositems.api.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cositems.api.dto.OrderRequestDTO;
import com.cositems.api.dto.OrderRequestDTO.OrderItemRequestDTO;
import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.exception.AuthorizationException;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.mapper.OrderMapper;
import com.cositems.api.model.Order;
import com.cositems.api.model.OrderItem;
import com.cositems.api.model.Product;
import com.cositems.api.model.User;
import com.cositems.api.repository.OrderRepository;
import com.cositems.api.repository.ProductRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final OrderMapper orderMapper;
    private final PaymentService paymentService;

    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest, String loggedInUserId) {
        List<Product> productsToUpdate = new ArrayList<>();

       List<OrderItem> orderItems = orderRequest.items().stream()
            .map(itemDto -> validateAndUpdateStock(itemDto, productsToUpdate))
            .collect(Collectors.toList());

        BigDecimal totalAmount = calculateTotal(orderItems);

        String transactionId = paymentService.processCreditCardPayment(totalAmount, orderRequest.paymentToken());

        productRepository.saveAll(productsToUpdate);

        Order order = orderMapper.toOrder(loggedInUserId, orderItems, transactionId);
        order.markAsPaid();

        Order savedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(savedOrder);
    }

    public Page<OrderResponseDTO> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable).map(orderMapper::toOrderResponseDTO);
    }

    public OrderResponseDTO getOrderById(String id, User loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        validateOwnershipOrAdmin(order, loggedInUser);

        return new OrderResponseDTO(order);
    }

    public Page<OrderResponseDTO> getUserOrders(String loggedInCustomerId, Pageable pageable) {
        Page<Order> ordersPage = orderRepository.findByUserId(loggedInCustomerId, pageable);
        return ordersPage.map(orderMapper::toOrderResponseDTO);
    }

    @Transactional
    public OrderResponseDTO markAsPaid(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));
        order.markAsPaid();

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO markAsShipped(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));
        order.markAsShipped();

        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(updatedOrder);
    }

    @Transactional
    public OrderResponseDTO cancelOrder(String id, User loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        validateOwnershipOrAdmin(order, loggedInUser);

        order.cancel();
        Order updatedOrder = orderRepository.save(order);

        return orderMapper.toOrderResponseDTO(updatedOrder);
    }

    private BigDecimal calculateTotal(List<OrderItem> items) {
        return items.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void validateOwnershipOrAdmin(Order order, User user) {
        boolean isOwner = order.getUserId().equals(user.getId());
        boolean isAdmin = user.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
        if (!isOwner && !isAdmin) {
            throw new AuthorizationException("Você não tem permissão para acessar este pedido.");
        }
    }

    private OrderItem validateAndUpdateStock(OrderItemRequestDTO itemDto, List<Product> productsToUpdate) {
        Product product = productRepository.findById(itemDto.productId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Produto com id " + itemDto.productId() + " não encontrado."));

        if (product.getQuantity() < itemDto.quantity()) {
            throw new BusinessRuleException("Estoque insuficiente para o produto: " + product.getName());
        }

        product.setQuantity(product.getQuantity() - itemDto.quantity());
        productsToUpdate.add(product);

        return orderMapper.toOrderItem(product, itemDto.quantity());
    }

}