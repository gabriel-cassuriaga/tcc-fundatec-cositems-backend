package com.cositems.api.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.cositems.api.model.UserModel;

public interface UserRepository extends MongoRepository<UserModel, String> {

}
