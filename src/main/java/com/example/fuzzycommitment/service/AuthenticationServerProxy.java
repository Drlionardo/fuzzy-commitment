package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.Otp;
import com.example.fuzzycommitment.repository.OtpRepo;
import com.example.fuzzycommitment.repository.UserRepo;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.security.SecureRandom;
import java.time.LocalDateTime;

@Service
public class AuthenticationServerProxy {
    private RestTemplate restTemplate;
    private UserRepo userRepo;
    private OtpRepo otpRepo;
    private BCryptPasswordEncoder passwordEncoder;
    @Value("${mailgun.api.url}")
    private String baseUrl;
    @Value("${mailgun.api.key}")
    private String mailgunKey;
    private final String VALID_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private final int CODE_LENGTH = 6;
    static SecureRandom random = new SecureRandom();

    public AuthenticationServerProxy(RestTemplate restTemplate, UserRepo userRepo, OtpRepo otpRepo, BCryptPasswordEncoder passwordEncoder) {
        this.restTemplate = restTemplate;
        this.userRepo = userRepo;
        this.otpRepo = otpRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean sendOtp(String username, String password) {
        System.out.println("SENDING OTP");
        var user =  userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
        if(passwordEncoder.matches(password, user.getPassword())) {
            return sendOtpToEmail(user.getEmail(), generateOTP(user.getId()));
        } else {
            throw new BadCredentialsException("Bad credentials for username=" + username);
        }
    }

    private boolean sendOtpToEmail(String email, String otp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", mailgunKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("from", "Fuzzy commitment<no-reply@mail.tannuki.me>");
        map.add("to", email);
        map.add("subject", "New login");
        map.add("text", otp);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        var response = restTemplate.postForEntity(baseUrl, request , Void.class);
        return response.getStatusCode().equals(HttpStatus.OK);
    }

    private String generateOTP(String userId) {
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < CODE_LENGTH; i++) {
            code.append(VALID_CHARACTERS.charAt(Math.abs(random.nextInt())%VALID_CHARACTERS.length()));
        }
        var otp = new Otp();
        otp.setUserId(userId);
        otp.setCreationDate(LocalDateTime.now());
        otp.setOtp(code.toString());
        otpRepo.save(otp);
        return code.toString();
    }

    public boolean checkOtp(String username, String otp) {
        var user =  userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username \"%s\" not found", username)));
        var otpFromDb = otpRepo.findByUserIdAndOtp(user.getId(), otp);
        if(otpFromDb.isPresent()) {
            otpRepo.delete(otpFromDb.get());
            return true;
        } else {
            return false;
        }
    }
}
