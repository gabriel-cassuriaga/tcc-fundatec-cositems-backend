package com.cositems.api.mapper;

import org.springframework.stereotype.Component;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.enums.UserType;
import com.cositems.api.model.Customer;
import com.cositems.api.model.Seller;
import com.cositems.api.model.User;

@Component
public class UserMapper {
    public UserResponseDTO toUserResponseDTO(User user) {
        return new UserResponseDTO(user.getId(), user.getDisplayName(), user.getEmail());
    }

    public User toUser(UserRequestDTO userRequest, String hashedPassword, UserType userType) {
        if (userType == UserType.CUSTOMER) {
            return Customer.builder()
                    .displayName(userRequest.displayName())
                    .email(userRequest.email())
                    .password(hashedPassword)
                    .build();
        } else if (userType == UserType.SELLER) {
            return Seller.builder()
                    .displayName(userRequest.displayName())
                    .email(userRequest.email())
                    .password(hashedPassword)
                    .build();
        } else {
            throw new IllegalArgumentException("Tipo de usuário '" + userType + "' não suportado pelo mapper.");
        }
    }
}