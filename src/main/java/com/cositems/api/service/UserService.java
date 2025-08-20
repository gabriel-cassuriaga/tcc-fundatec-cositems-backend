package com.cositems.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.model.Customer;
import com.cositems.api.model.Seller;
import com.cositems.api.model.UserModel;
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


    public UserResponseDTO getUserById(String id) {
        UserModel user = repository.findById(id).orElseThrow(() -> 
            new RuntimeException("Usuário não encontrado"));

        UserResponseDTO userResponse = new UserResponseDTO(user);
        return userResponse;

    }


    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
            .map(UserResponseDTO::new)
            .toList();

    }


    public void deleteUser(String id) {
        UserModel user = repository.findById(id).orElseThrow(() -> 
            new RuntimeException("Usuário não encontrado"));
        
        repository.delete(user);

    }

}
