package com.cositems.api.dto;

import com.cositems.api.model.UserModel;

public record UserResponseDTO(String id, String username, String email) {

    public UserResponseDTO(UserModel user) {
        this(user.getId(), user.getUsername(), user.getEmail());
    }
    
}
