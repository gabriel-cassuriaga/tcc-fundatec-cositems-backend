package com.cositems.api.user.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.exception.ValidationException;
import com.cositems.api.user.dto.UserRequestDTO;
import com.cositems.api.user.dto.UserResponseDTO;
import com.cositems.api.user.enums.UserType;
import com.cositems.api.user.model.Customer;
import com.cositems.api.user.model.Seller;
import com.cositems.api.user.model.User;
import com.cositems.api.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private void validateUserRequest(UserRequestDTO userRequest) {
        if (userRequest.displayName() == null || userRequest.displayName().isBlank()) {
            throw new ValidationException("O nome de usuário não pode ser vazio.");
        }

        if (userRequest.displayName().length() < 3 || userRequest.displayName().length() > 20) {
            throw new ValidationException("O nome de usuário deve ter entre 3 e 20 caracteres.");
        }

        String usernameRegex = "^[a-zA-Z0-9_.-]+$";
        if (!Pattern.matches(usernameRegex, userRequest.displayName())) {
            throw new ValidationException(
                    "O nome de usuário contém caracteres inválidos. Use apenas letras, números, underscore, hífen ou ponto.");
        }

        if (userRequest.email() == null || userRequest.email().isBlank()) {
            throw new ValidationException("O e-mail não pode ser vazio.");
        }

        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$";
        if (!Pattern.matches(emailRegex, userRequest.email())) {
            throw new ValidationException("O formato do e-mail é inválido.");
        }

        if (userRequest.password() == null || userRequest.password().isBlank()) {
            throw new ValidationException("A senha não pode ser vazia.");
        }

        if (userRequest.password().length() < 6) {
            throw new ValidationException("A senha deve ter no mínimo 6 caracteres.");
        }

    }

    private void checkUniqueness(UserRequestDTO userRequest) {
        if (userRepository.findByEmail(userRequest.email()).isPresent()) {
            throw new BusinessRuleException("O e-mail '" + userRequest.email() + "' já está em uso.");
        }
    }

    public UserResponseDTO createUser(UserRequestDTO userRequest, UserType userType) {
        validateUserRequest(userRequest);
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

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();

    }

    public void deleteUser(String id) {
        if (!userRepository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }

        userRepository.deleteById(id);

    }
}