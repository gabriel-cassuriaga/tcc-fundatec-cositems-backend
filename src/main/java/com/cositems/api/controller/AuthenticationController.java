package com.cositems.api.controller;

import com.cositems.api.dto.LoginRequestDTO;
import com.cositems.api.dto.LoginResponseDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.model.UserModel;
import com.cositems.api.service.TokenService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDTO> getLoggedInUser(Authentication authentication) {
        UserModel loggedInUser = (UserModel) authentication.getPrincipal();
        return ResponseEntity.ok(new UserResponseDTO(loggedInUser));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid LoginRequestDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        return ResponseEntity.ok(new LoginResponseDTO(token));
    }

   
}