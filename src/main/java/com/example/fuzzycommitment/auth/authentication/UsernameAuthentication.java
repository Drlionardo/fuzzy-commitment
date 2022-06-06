package com.example.fuzzycommitment.auth.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class UsernameAuthentication extends UsernamePasswordAuthenticationToken {

    public UsernameAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
