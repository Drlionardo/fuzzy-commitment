package com.example.fuzzycommitment.dto.request;

import lombok.Getter;

@Getter
public class CreateUser {
    private String username;
    private String password;
    private String email;
}
