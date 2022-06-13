package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class JwtService {
    @Value("${jwt.signing.key}")
    private String signingKey;

    public String buildJwt(User user) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(Map.of("username", user.getUsername(),
                        "client_id", user.getId()))
                .signWith(key)
                .compact();
    }
}
