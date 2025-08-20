package com.cositems.api.service;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.model.Customer;
import com.cositems.api.model.Seller;
import com.cositems.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public UserResponseDTO createCustomer(UserRequestDTO userRequest) {
        Customer customer = 
        Customer.builder()
            .username(userRequest.username())
            .email(userRequest.email())
            .password(userRequest.password())
            .build();

        repository.save(customer);

        UserResponseDTO userResponse = new UserResponseDTO(customer);
        return userResponse;
    }

    public UserResponseDTO createSeller(UserRequestDTO userRequest) {
        Seller seller = 
        Seller.builder()
            .username(userRequest.username())
            .email(userRequest.email())
            .password(userRequest.password())
            .build();

        repository.save(seller);

        UserResponseDTO userResponse = new UserResponseDTO(seller);
        return userResponse;
    }
}
