package com.cositems.api.user.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.user.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByDisplayName(String displayName);

    Optional<User> findByEmail(String email);
}