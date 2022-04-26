package com.example.fuzzycommitment.auth.filter;

import com.example.fuzzycommitment.auth.authentication.OtpAuthentication;
import com.example.fuzzycommitment.auth.authentication.UsernamePasswordAuthentication;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class InitialFilter extends OncePerRequestFilter {
    private AuthenticationManager manager;
    @Value("${jwt.signing.key}")
    private String signingKey;

    public InitialFilter(AuthenticationManager manager) {
        this.manager = manager;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = request.getHeader("username");
        String password = request.getHeader("password");
        String otp = request.getHeader("otp");

        if(otp == null) {
            var authentication = new UsernamePasswordAuthentication(username, password);
            manager.authenticate(authentication);
        } else {
            Authentication authentication = new OtpAuthentication(username, otp);
            manager.authenticate(authentication);

            String jwt = buildJwt(username);
            response.setHeader("Authorization", jwt);
        }
    }

    private String buildJwt(String username) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.builder()
                .setClaims(Map.of("username", username))
                .signWith(key)
                .compact();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
