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

            // String resetLink = "https://password-reset-page-alc.vercel.app/?token=" + token;
            String resetLink = "https://deb2025.github.io/password_reset_page/?token=" + token;

            String emailContent =
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<meta charset=\"UTF-8\">" +
                            "<title>Password Reset</title>" +
                            "<style>" +
                            "body { margin:0; padding:0; background-color:#f4f6f8; font-family:Arial, sans-serif; }" +
                            ".container { max-width:600px; margin:30px auto; background:#fff; border-radius:8px; box-shadow:0 4px 12px rgba(0,0,0,0.1); overflow:hidden; }" +
                            ".header { background:#2c3e50; text-align:center; padding:20px; }" +
                            ".header img { max-width:120px; }" +
                            ".content { padding:30px 20px; color:#333; }" +
                            ".content h3 { margin-top:0; color:#2c3e50; font-size:22px; }" +
                            ".content p { font-size:15px; line-height:1.6; margin-bottom:18px; }" +
                            ".btn { display:inline-block; padding:12px 24px; background:#ADD8E6; color:#e74c3c; text-decoration:none; border-radius:4px; font-weight:bold; }" +
                            ".footer { background:#ecf0f1; text-align:center; padding:15px; font-size:13px; color:#7f8c8d; }" +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class=\"container\">" +
                            "<div class=\"header\">" +
                            "<img src=\"https://iili.io/FAdFZjp.png\" alt=\"ALC Logo\" />" +
                            "</div>" +
                            "<div class=\"content\">" +
                            "<h3>[Art Law Communion] Password Reset</h3>" +
                            "<p>There has been a request for a password reset for:</p>" +
                            "<p><strong>Site Name:</strong> Art Law Communion</p>" +
                            "<p><strong>Username:</strong> " + user.getEmail() + "</p>" +
                            "<p>If you did not request a password reset, you can safely ignore this email.</p>" +
                            "<p style=\"text-align:center; margin:30px 0;\">" +
                            "<a class=\"btn\" href=\"" + resetLink + "\">Reset Your Password</a>" +
                            "</p>" +
                            "</div>" +
                            "<div class=\"footer\">" +
                            "© 2025 Art Law Communion • <a href=\"https://artlawcommunion.org\" style=\"color:#7f8c8d; text-decoration:none;\">Visit our website</a>" +
                            "</div>" +
                            "</div>" +
                            "</body>" +
                            "</html>";


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
