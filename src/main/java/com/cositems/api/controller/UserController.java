package com.cositems.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.service.UserService;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDTO> createSeller(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createSeller(userRequest);
        
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

        @PostMapping
    public ResponseEntity<UserResponseDTO> createCustomer(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createCustomer(userRequest);
        
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }
    
}
