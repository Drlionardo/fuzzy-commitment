package com.example.fuzzycommitment.dto.request;

import lombok.Getter;

@Getter
public class CreateUserDto {
    private String username;
    private String email;
    private String password;
}
