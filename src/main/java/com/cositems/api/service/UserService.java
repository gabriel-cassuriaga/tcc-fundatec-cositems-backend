package com.cositems.api.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.enums.UserType;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.model.Customer;
import com.cositems.api.model.Seller;
import com.cositems.api.model.User;
import com.cositems.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void checkUniqueness(UserRequestDTO userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new BusinessRuleException("O e-mail '" + userRequest.email() + "' já está em uso.");
        }
    }

    public UserResponseDTO createUser(UserRequestDTO userRequest, UserType userType) {
        checkUniqueness(userRequest);

        String hashedPassword = passwordEncoder.encode(userRequest.password());

        User userToSave;

        if (userType == UserType.CUSTOMER) {
            userToSave = Customer.builder()
                    .displayName(userRequest.displayName())
                    .email(userRequest.email())
                    .password(hashedPassword)
                    .build();
        } else if (userType == UserType.SELLER) {
            userToSave = Seller.builder()
                    .displayName(userRequest.displayName())
                    .email(userRequest.email())
                    .password(hashedPassword)
                    .build();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + userType);
        }

        User savedUser = userRepository.save(userToSave);
        return new UserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

        return new UserResponseDTO(user);

    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(UserResponseDTO::new);

    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }

        userRepository.deleteById(id);

    }
}