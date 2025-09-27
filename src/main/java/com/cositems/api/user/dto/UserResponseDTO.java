package com.cositems.api.user.dto;

import com.cositems.api.user.model.User;

public record UserResponseDTO(String id, String displayName, String email) {

    public UserResponseDTO(User user) {
        this(user.getId(), user.getDisplayName(), user.getEmail());
    }
}