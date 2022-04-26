package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.User;
import com.example.fuzzycommitment.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {
    private UserRepo userRepo;
    private BCryptPasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
    }

    public void registerUser(String username, String password, String email) {
        var user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email).build();
        userRepo.save(user);

    }

    public void deleteUser() {

    }
}
