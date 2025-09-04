package com.cositems.api.controller;

import com.cositems.api.dto.OrderRequestDTO;
import com.cositems.api.dto.OrderResponseDTO;
import com.cositems.api.service.OrderService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody OrderRequestDTO orderRequest) {
        OrderResponseDTO orderResponse = orderService.createOrder(orderRequest);
        return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);

    }


    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAllOrders() {
        List<OrderResponseDTO> orders = orderService.getAllOrders();
        return new ResponseEntity<>(orders, HttpStatus.OK);

    }


    @PutMapping("/{id}/pay")
    public ResponseEntity<OrderResponseDTO> markAsPaid(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.markAsPaid(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @PutMapping("/{id}/ship")
    public ResponseEntity<OrderResponseDTO> markAsShipped(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.markAsShipped(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.getOrderById(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }


    @PutMapping("/{id}/cancel")
    public ResponseEntity<OrderResponseDTO> cancelOrder(@PathVariable String id) {
        OrderResponseDTO orderResponse = orderService.cancelOrder(id);
        return new ResponseEntity<>(orderResponse, HttpStatus.OK);

    }

}