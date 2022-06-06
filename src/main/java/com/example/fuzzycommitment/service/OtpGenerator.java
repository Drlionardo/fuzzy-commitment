package com.example.fuzzycommitment.service;


import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class OtpGenerator {
    private final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int CODE_LENGTH = 6;
    private static SecureRandom random = new SecureRandom();

    public String generateOTP() {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(VALID_CHARACTERS.charAt(Math.abs(random.nextInt())%VALID_CHARACTERS.length()));
        }
        return code.toString();
    }


}
