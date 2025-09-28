package com.cositems.api.order.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.order.dto.OrderRequestDTO;
import com.cositems.api.order.dto.OrderResponseDTO;
import com.cositems.api.order.service.OrderService;
import com.cositems.api.user.model.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private String getLoggedInUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody @Valid OrderRequestDTO orderRequest,
            Authentication authentication) {
        String loggedInCustomerId = getLoggedInUserId(authentication);

        OrderResponseDTO orderResponse = orderService.createOrder(orderRequest, loggedInCustomerId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String orderId, Authentication authentication) {
        User loggedInUser = (User) authentication.getPrincipal();

        OrderResponseDTO orderResponse = orderService.getOrderById(orderId, loggedInUser);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        Page<OrderResponseDTO> ordersPage = orderService.getAllOrders(pageable);
        return new ResponseEntity<>(ordersPage, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-orders")
    public ResponseEntity<Page<OrderResponseDTO>> getUserOrders(Pageable pageable, Authentication authentication) {
        String loggedInCustomerId = getLoggedInUserId(authentication);
        Page<OrderResponseDTO> ordersPage = orderService.getUserOrders(loggedInCustomerId, pageable);
        return new ResponseEntity<>(ordersPage, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponseDTO> markAsPaid(@PathVariable String orderId) {
        OrderResponseDTO orderResponse = orderService.markAsPaid(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponseDTO> markAsShipped(@PathVariable String orderId) {
        OrderResponseDTO orderResponse = orderService.markAsShipped(orderId);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable String orderId, Authentication authentication) {
        User loggedInUser = (User) authentication.getPrincipal();

        OrderResponseDTO orderResponse = orderService.cancelOrder(orderId, loggedInUser);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }

}