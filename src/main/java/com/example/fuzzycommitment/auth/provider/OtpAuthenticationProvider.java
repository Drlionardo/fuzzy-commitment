package com.example.fuzzycommitment.auth.provider;

import com.example.fuzzycommitment.auth.authentication.OtpAuthentication;
import com.example.fuzzycommitment.service.AuthenticationService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OtpAuthenticationProvider implements AuthenticationProvider {
    private AuthenticationService proxy;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String otp = String.valueOf(authentication.getCredentials());
        boolean result = proxy.checkOtp(username, otp);

        if (result) {
            return new OtpAuthentication(username, otp);
        } else {
            throw new BadCredentialsException("Invalid otp or username");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return OtpAuthentication.class.isAssignableFrom(authentication);
    }
}
