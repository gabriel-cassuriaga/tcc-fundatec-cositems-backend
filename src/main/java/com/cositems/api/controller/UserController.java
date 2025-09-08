package com.cositems.api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.service.UserService;

import lombok.AllArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/sellers")
    public ResponseEntity<UserResponseDTO> createSeller(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createSeller(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }


    @PostMapping("/customers")
    public ResponseEntity<UserResponseDTO> createCustomer(@RequestBody UserRequestDTO userRequest) {
        UserResponseDTO userResponse = userService.createCustomer(userRequest);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);

    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAll() {
        List<UserResponseDTO> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);

    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }

}
