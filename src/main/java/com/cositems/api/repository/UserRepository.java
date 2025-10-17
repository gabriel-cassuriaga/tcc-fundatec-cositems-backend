package com.cositems.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByDisplayName(String displayName);

    Optional<User> findByEmail(String email);
}