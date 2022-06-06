package com.example.fuzzycommitment.service;

import com.example.fuzzycommitment.entity.Otp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class MailService {
    @Autowired
    private RestTemplate restTemplate;
    @Value("${mailgun.api.url}")
    private String baseUrl;
    @Value("${mailgun.api.key}")
    private String mailgunKey;

    public boolean sendOtpToEmail(String email, Otp otp) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth("api", mailgunKey);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> map= new LinkedMultiValueMap<>();
        map.add("from", "Fuzzy commitment<no-reply@mail.tannuki.me>");
        map.add("to", email);
        map.add("subject", "New login");
        map.add("text", otp.getOtp());

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(map, headers);

        var response = restTemplate.postForEntity(baseUrl, request , String.class);
        log.info("Mailgun service response {}", response.getBody());
        return response.getStatusCode().equals(HttpStatus.OK);
    }
}
