package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreateUser;
import com.example.fuzzycommitment.repository.UserRepo;
import com.example.fuzzycommitment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;

    @PutMapping("/register")
    public void registerUser(@RequestBody CreateUser userDto) {
        userService.registerUser(userDto.getUsername(), userDto.getPassword(), userDto.getEmail());
    }

    @PostMapping("/login")
    public void loginUser() {

    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser() {

    }
}
