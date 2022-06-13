package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.Otp;
import com.example.fuzzycommitment.repository.OtpRepo;
import com.example.fuzzycommitment.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@AllArgsConstructor
@Service
public class AuthenticationService {
    private BCryptPasswordEncoder passwordEncoder;
    private UserService userService;
    private OtpGenerator otpGenerator;
    private OtpRepo otpRepo;
    private MailService mailService;


    public boolean sendOtpByEmail(String email, String password) {
        var user = userService.loadUserByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            Otp otp = createOtp(user.getId());
            return mailService.sendOtpToEmail(user.getEmail(), otp);
        } else {
            throw new BadCredentialsException(String.format("Bad credentials for email %s", email));
        }
    }

    public boolean sendOtpToValidateEmail(String email, String password) {
        var user = userService.loadUserByEmail(email);
        if (passwordEncoder.matches(password, user.getPassword())) {
            Otp otp = createOtp(user.getId());
            return mailService.sendOtpToValidateEmailAfterRegistration(user.getEmail(), otp);
        } else {
            throw new BadCredentialsException(String.format("Bad credentials for email %s", email));
        }
    }

    public boolean checkOtp(String email, String otp) {
        var user = userService.loadUserByEmail(email);
        var otpFromDb = otpRepo.findByUserIdAndOtp(user.getId(), otp);
        if (otpFromDb.isPresent()) {
            otpRepo.delete(otpFromDb.get()); //todo: May cause error with retry -> invalidate by timeout
            return true;
        } else {
            return false;
        }
    }

    public Otp createOtp(String userId) {
        var otp = new Otp();
        otp.setUserId(userId);
        otp.setCreationDate(LocalDateTime.now());
        otp.setOtp(otpGenerator.generateOTP());
        otpRepo.save(otp);
        return otp;
    }
}
