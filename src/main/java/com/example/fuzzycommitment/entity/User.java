package com.example.fuzzycommitment.entity;

import org.springframework.data.annotation.Id;

import java.math.BigInteger;

public class User  {
    @Id
    private BigInteger id;
    private String username;
    private String password;

}
