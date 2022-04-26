package com.example.fuzzycommitment.repository;

import com.example.fuzzycommitment.entity.Otp;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OtpRepo extends MongoRepository<Otp, String> {
    Optional<Otp> findByUserIdAndOtp(String userId, String otp);
}
