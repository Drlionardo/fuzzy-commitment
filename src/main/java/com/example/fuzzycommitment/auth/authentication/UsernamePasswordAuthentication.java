package com.example.fuzzycommitment.auth.authentication;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
public class UsernamePasswordAuthentication extends UsernamePasswordAuthenticationToken {

    public UsernamePasswordAuthentication(Object principal, Object credentials) {
        super(principal, credentials);
    }
}
