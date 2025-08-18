package com.cositems.api.service;

import org.springframework.stereotype.Service;

import com.cositems.api.dto.UserRequestDTO;
import com.cositems.api.model.UserModel;
import com.cositems.api.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserRepository repository;

    public UserModel createUser(UserRequestDTO userRequest) {
        
        UserModel user = new UserModel(
            userRequest.username(),
            userRequest.email(),
            userRequest.password()
        );

        repository.save(user);

        return user;
    }
}
