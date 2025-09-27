package com.cositems.api.controller;

import com.cositems.api.dto.OrderRequestDTO;
import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.model.UserModel;
import com.cositems.api.service.OrderService;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    private String getLoggedInUserId(Authentication authentication) {
        UserModel user = (UserModel) authentication.getPrincipal();
        return user.getId();
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest,
            Authentication authentication) {
        String loggedInCustomerId = getLoggedInUserId(authentication);

        OrderResponseDTO orderResponse = orderService.createOrder(orderRequest, loggedInCustomerId);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

    }

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String orderId, Authentication authentication) {
        UserModel loggedInUser = (UserModel) authentication.getPrincipal();

        OrderResponseDTO orderResponse = orderService.getOrderById(orderId, loggedInUser);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderResponseDTO>> getUserOrders(Authentication authentication) {
        String loggedInCustomerId = getLoggedInUserId(authentication);
        List<OrderResponseDTO> orders = orderService.getUserOrders(loggedInCustomerId);
        return new ResponseEntity<>(orders, HttpStatus.OK);

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
        UserModel loggedInUser = (UserModel) authentication.getPrincipal();

        OrderResponseDTO orderResponse = orderService.cancelOrder(orderId, loggedInUser);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }

}