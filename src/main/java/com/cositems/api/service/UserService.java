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
        Customer customer = Customer.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(userRequest.password())
                .build();

        Customer savedUser = repository.save(customer);

        return new UserResponseDTO(savedUser);

    }


    public UserResponseDTO createSeller(UserRequestDTO userRequest) {
        Seller seller = Seller.builder()
                .username(userRequest.username())
                .email(userRequest.email())
                .password(userRequest.password())
                .build();

        Seller savedUser = repository.save(seller);

        return new UserResponseDTO(savedUser);

    }


    public UserResponseDTO getUserById(String id) {
        UserModel user = repository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        return new UserResponseDTO(user);

    }


    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();

    }

    
    public void deleteUser(String id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado com o id: " + id);
        }

        repository.deleteById(id);

    }

}
