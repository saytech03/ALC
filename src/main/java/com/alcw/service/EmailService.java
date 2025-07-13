package com.alcw.service;


import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactRequest;
import com.alcw.model.User;

public interface EmailService {
    void sendOTPEmail(String email, String name, String otp);
    void sendWelcomeEmail(User user);
    void sendContactConfirmation(ContactRequestDTO contactDto);
    void sendContactNotification(ContactRequest contactRequest);
}
