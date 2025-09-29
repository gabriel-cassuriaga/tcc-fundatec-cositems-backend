package com.cositems.api.admin.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.order.dto.OrderResponseDTO;
import com.cositems.api.order.service.OrderService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(Pageable pageable) {
        Page<OrderResponseDTO> ordersPage = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(ordersPage);
    }

    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponseDTO> markAsPaid(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.markAsPaid(id);
        return ResponseEntity.ok(orderResponse);
    }

    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponseDTO> markAsShipped(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.markAsShipped(id);
        return ResponseEntity.ok(orderResponse);
    }
}