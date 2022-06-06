package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreateUserDto;
import com.example.fuzzycommitment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;

    @PostMapping("/register")
    public String registerUser(@RequestBody CreateUserDto userDto) {
        userService.registerUser(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        return String.format("Successfully registered user %s", userDto.getUsername());
    }

    @PostMapping("/login")
    public void loginUser() {

    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser() {

    }
}
