package com.alcw.service;

import com.alcw.dto.PasswordResetDTO;
import com.alcw.exception.InvalidCredentialsException;
import com.alcw.model.User;
import com.alcw.repository.UserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService {

    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final PasswordEncoder passwordEncoder;

    public void requestReset(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        String token = UUID.randomUUID().toString();
        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusHours(1));
        userRepository.save(user);

        sendResetEmail(user, token);
    }

    public void resetPassword(PasswordResetDTO dto) {
        if (!dto.getNewPassword().equals(dto.getConfirmPassword())) {
            throw new InvalidCredentialsException("Passwords do not match");
        }

        User user = userRepository.findByResetToken(dto.getToken())
                .orElseThrow(() -> new InvalidCredentialsException("Invalid token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new InvalidCredentialsException("Token has expired");
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setResetToken(null);
        user.setResetTokenExpiry(null);
        userRepository.save(user);
    }

    private void sendResetEmail(User user, String token) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            String resetLink = "https://deb2025.github.io/password_reset_page/?token=" + token;

            String emailContent = "<h3>[Art Law Communion] Password Reset</h3>" +
                    "<p>There has been a request for password reset for:</p>" +
                    "<p>Site Name: Art Law Communion</p>" +
                    "<p>Username: " + user.getEmail() + "</p>" +
                    "<p>If this was a mistake, please ignore this email.</p>" +
                    "<p><a href=\"" + resetLink + "\">Click here to reset your password</a></p>";

            helper.setTo(user.getEmail());
            helper.setSubject("[Art Law Communion] Password Reset");
            helper.setText(emailContent, true);
            helper.setFrom("noreply@artlawcommunion.com");

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send reset email", e);
        }
    }
}
