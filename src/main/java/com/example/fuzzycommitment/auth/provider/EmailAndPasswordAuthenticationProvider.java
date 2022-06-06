package com.example.fuzzycommitment.auth.provider;

import com.example.fuzzycommitment.auth.authentication.EmailAuthentication;
import com.example.fuzzycommitment.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class EmailAndPasswordAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationService authService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String email = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        authService.sendOtpByEmail(email, password);

        return new EmailAuthentication(email, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return EmailAuthentication.class.isAssignableFrom(authentication);
    }
}
