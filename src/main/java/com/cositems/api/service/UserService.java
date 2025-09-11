package com.cositems.api.service;

import java.util.List;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.dto.UserResponseDTO;
import com.cositems.api.enums.UserType;
import com.cositems.api.exception.BusinessRuleException;
import com.cositems.api.exception.ResourceNotFoundException;
import com.cositems.api.exception.ValidationException;
import com.cositems.api.model.Customer;
import com.cositems.api.model.Seller;
import com.cositems.api.model.UserModel;
import com.cositems.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    private void validateUserRequest(UserRequestDTO userRequest) {
        if (userRequest.username() == null || userRequest.username().isBlank()) {
            throw new ValidationException("O nome de usuário não pode ser vazio.");
        }

        if (userRequest.username().length() < 3 || userRequest.username().length() > 10) {
            throw new ValidationException("O nome de usuário deve ter entre 3 e 10 caracteres.");
        }

        String usernameRegex = "^[a-zA-Z0-9_.-]+$";
        if (!Pattern.matches(usernameRegex, userRequest.username())) {
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
        if (repository.findByUsername(userRequest.username()).isPresent()) {
            throw new BusinessRuleException("O nome de usuário '" + userRequest.username() + "' já está em uso.");
        }

        if (repository.findByEmail(userRequest.email()).isPresent()) {
            throw new BusinessRuleException("O e-mail '" + userRequest.email() + "' já está em uso.");
        }
    }


     public UserResponseDTO createUser(UserRequestDTO userRequest, UserType userType) {
        validateUserRequest(userRequest);
        checkUniqueness(userRequest);

        UserModel userToSave;

        if (userType == UserType.CUSTOMER) {
            userToSave = Customer.builder()
                    .username(userRequest.username())
                    .email(userRequest.email())
                    .password(userRequest.password())
                    .build();
        } else if (userType == UserType.SELLER) {
            userToSave = Seller.builder()
                    .username(userRequest.username())
                    .email(userRequest.email())
                    .password(userRequest.password())
                    .build();
        } else {
            throw new IllegalArgumentException("Tipo de usuário inválido: " + userType);
        }

        UserModel savedUser = repository.save(userToSave);
        return new UserResponseDTO(savedUser);
    }


    public UserResponseDTO getUserById(String id) {
        UserModel user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado com o id: " + id));

        return new UserResponseDTO(user);

    }


    public List<UserResponseDTO> getAllUsers() {
        return repository.findAll().stream()
                .map(UserResponseDTO::new)
                .toList();

    }


    public void deleteUser(String id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Usuário não encontrado com o id: " + id);
        }

        repository.deleteById(id);

    }


}
