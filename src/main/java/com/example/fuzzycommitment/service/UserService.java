package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.User;
import com.example.fuzzycommitment.exception.EmailAlreadyExistsException;
import com.example.fuzzycommitment.exception.InvalidOtpException;
import com.example.fuzzycommitment.exception.UsernameAlreadyExistsException;
import com.example.fuzzycommitment.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private AuthenticationService authenticationService;
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public User loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
    }

    public User loadUserByEmail(String email) throws UsernameNotFoundException {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Email \"%s\" not found", email)));
    }

    public String getEmailByUsername(String username) {
        var user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
        return user.getEmail();
    }

    public User registerUser(String username, String email, String password) {
        if (userRepo.existsByUsername(username)) {
            throw new UsernameAlreadyExistsException(username);
        } else if (userRepo.existsByEmail(email)) {
            throw new EmailAlreadyExistsException(email);
        }

        var user = User.builder()
                .isEmailValidated(false)
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email).build();
        user = userRepo.save(user);

        authenticationService.sendOtpToValidateEmail(email, password);

        return user;
    }

    public User validateUser (String email, String otp) {
        var user = loadUserByEmail(email);
        if (authenticationService.checkOtp(email, otp)) {
            user.setEmailValidated(true);
        } else {
            throw new InvalidOtpException(email);
        }
        return userRepo.save(user);
    }

}
