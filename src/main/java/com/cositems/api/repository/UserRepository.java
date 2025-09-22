package com.cositems.api.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.UserModel;

public interface UserRepository extends MongoRepository<UserModel, String> {
    Optional<UserModel> findByDisplayName(String displayName);

    Optional<UserModel> findByEmail(String email);

}
