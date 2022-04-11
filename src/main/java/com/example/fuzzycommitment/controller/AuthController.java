package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreateUser;
import org.springframework.web.bind.annotation.*;

@RestController
public class AuthController {

    @PutMapping("/register")
    public void registerUser(@RequestBody CreateUser userDto) {

    }

    @PostMapping("/login")
    public void loginUser() {

    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser() {

    }
}
