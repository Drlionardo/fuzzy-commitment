package com.example.fuzzycommitment.auth.filter;

import com.example.fuzzycommitment.auth.authentication.EmailAuthentication;
import com.example.fuzzycommitment.auth.authentication.OtpAuthentication;
import com.example.fuzzycommitment.auth.authentication.UsernameAuthentication;
import com.example.fuzzycommitment.dto.request.LoginUserDto;
import com.example.fuzzycommitment.entity.User;
import com.example.fuzzycommitment.service.AuthenticationService;
import com.example.fuzzycommitment.service.JwtService;
import com.example.fuzzycommitment.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Component
public class InitialLoginFilter extends OncePerRequestFilter {
    private AuthenticationManager manager;
    private UserService userService;
    private JwtService jwtService;

    public InitialLoginFilter(AuthenticationManager manager, UserService userService, JwtService jwtService) {
        this.manager = manager;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException {
        byte[] body = StreamUtils.copyToByteArray(request.getInputStream());
        LoginUserDto loginUserDto = new ObjectMapper().readValue(body, LoginUserDto.class);

        String email = getEmail(loginUserDto);
        String password = loginUserDto.getPassword();
        String otp = loginUserDto.getOtp();

        if (otp == null) {
            var authentication = new EmailAuthentication(email, password);
            manager.authenticate(authentication);
        } else {
            var  authentication = new OtpAuthentication(email, otp);
            manager.authenticate(authentication);

            var user = userService.loadUserByEmail(email);
            String jwt = jwtService.buildJwt(user);
            response.setHeader("Authorization", jwt);
        }
    }

    private String getEmail(LoginUserDto loginUserDto) {
        String email = loginUserDto.getEmail();
        if (email == null || email.isEmpty()) {
            String username = loginUserDto.getUsername();
            email = userService.getEmailByUsername(username);
        }
        return email;
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/login");
    }
}
