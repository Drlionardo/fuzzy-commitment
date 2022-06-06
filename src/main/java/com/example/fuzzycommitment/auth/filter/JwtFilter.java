package com.example.fuzzycommitment.auth.filter;

import com.example.fuzzycommitment.auth.authentication.JwtAuthentication;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    @Value("${jwt.signing.key}")
    private String signingKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String jwt = getJwtFromHeader(request);
        Claims claims = getClaims(jwt);

        String username = String.valueOf(claims.get("username"));

        GrantedAuthority authority = new SimpleGrantedAuthority("user");
        var auth = new JwtAuthentication(username, null, List.of(authority));
        auth.setUserId(claims.get("client_id", String.class));

        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request, response);
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        var headerParts = request.getHeader("Authorization").split(" ");
        //todo: validate if headerParts is not empty
        if(headerParts[0].equals("Bearer")) {
            return headerParts[1];
        } else {
            throw new BadCredentialsException("Invalid Authentication token type, expected Bearer, got " + headerParts[0]);
        }
    }

    private Claims getClaims(String jwt) {
        SecretKey key = Keys.hmacShaKeyFor(signingKey.getBytes(StandardCharsets.UTF_8));
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(jwt)
                .getBody();
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        var path = request.getServletPath();
        return path.equals("/login") || path.equals("/register");
    }
}
