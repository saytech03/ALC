package com.alcw.service;


public interface OTPService {
    String generateOTP(String email);
    void validateOTP(String email, String otp);
    void clearOTP(String email);
}
