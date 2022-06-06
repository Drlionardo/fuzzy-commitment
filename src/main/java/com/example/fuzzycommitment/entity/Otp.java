package com.example.fuzzycommitment.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

@Data
public class Otp {
    @Id
    private String id;
    private String userId;
    private String otp;
    private LocalDateTime creationDate;

    public boolean isExpired() {
        //todo: expire with timeout
        return true;
    }
}
