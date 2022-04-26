package com.example.fuzzycommitment.repository;

import com.example.fuzzycommitment.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepo extends MongoRepository<User, String> {
    Optional<User> findByUsername(String s);
}
