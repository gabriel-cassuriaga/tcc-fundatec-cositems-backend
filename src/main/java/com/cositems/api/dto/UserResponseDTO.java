package com.cositems.api.dto;

import com.cositems.api.model.User;

public record UserResponseDTO(String id, String displayName, String email) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getDisplayName(), user.getEmail());
    }
}