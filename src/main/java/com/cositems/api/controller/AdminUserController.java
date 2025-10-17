package com.cositems.api.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAll(Pageable pageable) {
        Page<UserResponseDTO> usersPage = userService.getAllUsers(pageable);
        return new ResponseEntity<>(usersPage, HttpStatus.OK);
    }
}