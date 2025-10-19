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
import com.cositems.api.mapper.UserMapper;
import com.cositems.api.model.User;
import com.cositems.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    public UserResponseDTO createUser(UserRequestDTO userRequest, UserType userType) {
        checkUniqueness(userRequest);

        String hashedPassword = passwordEncoder.encode(userRequest.password());
        User userToSave = userMapper.toUser(userRequest, hashedPassword, userType);
        User savedUser = userRepository.save(userToSave);

        return userMapper.toUserResponseDTO(savedUser);
    }

    public UserResponseDTO getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

        return userMapper.toUserResponseDTO(user);
    }

    public Page<UserResponseDTO> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable).map(userMapper::toUserResponseDTO);
    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }

        userRepository.deleteById(id);
    }

    private void checkUniqueness(UserRequestDTO userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new BusinessRuleException("O e-mail '" + userRequest.email() + "' já está em uso.");
        }
    }
}