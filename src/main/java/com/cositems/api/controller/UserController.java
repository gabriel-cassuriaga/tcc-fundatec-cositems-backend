package com.cositems.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.enums.UserType;
import com.cositems.api.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UserResponseDTO userResponse = userService.getUserById(id);
        return new ResponseEntity<>(userResponse, HttpStatus.OK);
    }

    @PostMapping("/register/{userType}")
    public ResponseEntity<UserResponseDTO> createUser(@RequestBody @Valid UserRequestDTO userRequest,
            @PathVariable UserType userType) {
        UserResponseDTO userResponse = userService.createUser(userRequest, userType);
        return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
    }

    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);

    }
}