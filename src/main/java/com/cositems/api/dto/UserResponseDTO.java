package com.cositems.api.dto;

import com.cositems.api.model.UserModel;

public record UserResponseDTO(String username, String email) {

    public UserResponseDTO(UserModel user) {
        this(user.getUsername(), user.getEmail());
    }
    
}
