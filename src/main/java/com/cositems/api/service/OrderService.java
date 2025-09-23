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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderResponseDTO createOrder(OrderRequestDTO orderRequest, UserModel loggedInUser) {
        if (orderRequest.items() == null || orderRequest.items().isEmpty()) {
            throw new ValidationException("O pedido deve conter pelo menos um item.");
        }

        List<Order.OrderItem> orderItems = orderRequest.items().stream()
                .map(itemDto -> {
                    ProductModel product = productRepository.findById(itemDto.productId())
                            .orElseThrow(() -> new ResourceNotFoundException("Produto com id " + itemDto.productId() + " não encontrado."));

                    if (product.getQuantity() < itemDto.quantity()) {
                        throw new BusinessRuleException("Estoque insuficiente para o produto: " + product.getName());
                    }
                    
                    product.setQuantity(product.getQuantity() - itemDto.quantity());
                    productRepository.save(product);

                    Order.OrderItem item = new Order.OrderItem();
                    item.setProductId(product.getId());
                    item.setName(product.getName());
                    item.setQuantity(itemDto.quantity());
                    item.setPrice(product.getPrice());
                    return item;
                }).collect(Collectors.toList());

        BigDecimal total = orderItems.stream()
                .map(item -> item.getPrice().multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(loggedInUser.getId())
                .orderDate(LocalDateTime.now())
                .status(OrderStatus.PENDING)
                .items(orderItems)
                .total(total)
                .build();

        Order savedOrder = orderRepository.save(order);
        return new OrderResponseDTO(savedOrder);
    }

    public List<OrderResponseDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(OrderResponseDTO::new)
                .toList();
    }
    
    public OrderResponseDTO getOrderById(String id, UserModel loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido não encontrado com o id: " + id));

        boolean isOwner = order.getUserId().equals(loggedInUser.getId());
        boolean isAdmin = loggedInUser.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AuthorizationException("Você не tem permissão para visualizar este pedido.");
        }

        return new OrderResponseDTO(order);
    }
    
    public OrderResponseDTO markAsPaid(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido не encontrado com o id: " + id));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Apenas pedidos pendentes podem ser marcados como pagos. Status atual: " + order.getStatus());
        }

        order.setStatus(OrderStatus.PAID);
        return new OrderResponseDTO(orderRepository.save(order));
    }

    public OrderResponseDTO markAsShipped(String id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido не encontrado com o id: " + id));

        if (order.getStatus() != OrderStatus.PAID) {
            throw new BusinessRuleException("Apenas pedidos pagos podem ser marcados como enviados. Status atual: " + order.getStatus());
        }

        order.setStatus(OrderStatus.SHIPPED);
        return new OrderResponseDTO(orderRepository.save(order));
    }
    
    public OrderResponseDTO cancelOrder(String id, UserModel loggedInUser) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pedido не encontrado com o id: " + id));

        boolean isOwner = order.getUserId().equals(loggedInUser.getId());
        boolean isAdmin = loggedInUser.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        if (!isOwner && !isAdmin) {
            throw new AuthorizationException("Você não tem permissão para cancelar este pedido.");
        }

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new BusinessRuleException("Apenas pedidos pendentes podem ser cancelados. Status atual: " + order.getStatus());
        }
        
        for (Order.OrderItem item : order.getItems()) {
            productRepository.findById(item.getProductId()).ifPresent(product -> {
                product.setQuantity(product.getQuantity() + item.getQuantity());
                productRepository.save(product);
            });
        }

        order.setStatus(OrderStatus.CANCELLED);
        return new OrderResponseDTO(orderRepository.save(order));
    }
}