package com.example.fuzzycommitment.controller;

import com.example.fuzzycommitment.dto.request.CreateUserDto;
import com.example.fuzzycommitment.dto.request.LoginUserDto;
import com.example.fuzzycommitment.service.JwtService;
import com.example.fuzzycommitment.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@AllArgsConstructor
public class AuthController {
    private UserService userService;
    private JwtService jwtService;

    @PostMapping("/register")
    public String registerUser(@RequestBody CreateUserDto userDto) {
        userService.registerUser(userDto.getUsername(), userDto.getEmail(), userDto.getPassword());
        return String.format("Please enter otp code that was sent to %s email", userDto.getEmail());

    }
    @PostMapping("/register/validate")
    public ResponseEntity<String> validateEmail(@RequestBody LoginUserDto dto) {
        var user = userService.validateUser(dto.getEmail(), dto.getOtp());
        String jwt = jwtService.buildJwt(user);
        return ResponseEntity.ok()
                .header("Authorization", jwt)
                .body(String.format("Successfully validated email %s", dto.getEmail()));
    }

    @PostMapping("/login")
    public String loginUser() {
        //All logic implemented in filters
        return "Otp was sent to email";
    }

    @DeleteMapping("/user/{id}/delete")
    public void deleteUser() {
        //todo: delete all user info and resources

    }
}
