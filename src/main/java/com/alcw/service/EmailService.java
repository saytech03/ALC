package com.alcw.service;


import com.alcw.model.User;

public interface EmailService {
    void sendOTPEmail(String email, String name, String otp);
    void sendWelcomeEmail(User user);
}
