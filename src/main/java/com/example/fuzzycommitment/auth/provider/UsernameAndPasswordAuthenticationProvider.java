package com.example.fuzzycommitment.auth.provider;

import com.example.fuzzycommitment.auth.authentication.UsernameAuthentication;
import com.example.fuzzycommitment.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UsernameAndPasswordAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationService proxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = String.valueOf(authentication.getCredentials());

        proxy.sendOtpByUsername(username, password);

        return new UsernameAuthentication(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernameAuthentication.class.isAssignableFrom(authentication);
    }
}
