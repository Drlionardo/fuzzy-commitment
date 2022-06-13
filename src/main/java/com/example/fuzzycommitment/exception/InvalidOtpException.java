package com.example.fuzzycommitment.exception;

public class InvalidOtpException extends RuntimeException{
    public InvalidOtpException(String email) {
        super(String.format("Bad Otp for %s email", email));
    }
}
