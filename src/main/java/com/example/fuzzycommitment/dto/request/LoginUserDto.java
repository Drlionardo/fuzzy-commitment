package com.example.fuzzycommitment.dto.request;

import lombok.Getter;
@Getter
public class LoginUserDto {
    private String email;
    private String username;
    private String password;
    private String otp;
}
