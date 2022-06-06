package com.example.fuzzycommitment.auth.authentication;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

public class EmailAuthentication extends UsernamePasswordAuthenticationToken {

    public EmailAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
