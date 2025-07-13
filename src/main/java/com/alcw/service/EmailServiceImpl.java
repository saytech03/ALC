package com.alcw.service;


import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactRequest;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.alcw.model.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${spring.mail.username}")
    private String fromEmail;

    public EmailServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    @Override
    public void sendOTPEmail(String email, String name, String otp) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", name); // Add name to context
            context.setVariable("otp", otp);
            String htmlContent = templateEngine.process("otp-email", context);

            helper.setFrom(fromEmail);
            helper.setTo(email);
            helper.setSubject("Email Verification");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send OTP email", e);
        }
    }

    @Override
    public void sendWelcomeEmail(User user) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            Context context = new Context();
            context.setVariable("name", user.getName());
            context.setVariable("membershipId", user.getMembershipId());
            String htmlContent = templateEngine.process("welcome-email", context);

//            // Attach logo as inline resource
//            Resource logo = new ClassPathResource("static/images/alc-logo.png");
//            helper.addInline("logo", logo);
//
//            // Attach brochure
//            Resource brochure = new ClassPathResource("static/docs/ALC_Brochure.pdf");
//            helper.addAttachment("ALC_Welcome_Brochure.pdf", brochure);

            helper.setFrom(fromEmail);
            helper.setTo(user.getEmail());
            helper.setSubject("Welcome to the Art Law Communion");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send welcome email", e);
        }
    }

    // Add to your EmailServiceImpl class
    @Override
    public void sendContactConfirmation(ContactRequestDTO contactDto) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", contactDto.getName());
            String htmlContent = templateEngine.process("contact-confirmation", context);

            // Attach logo
//            Resource logo = new ClassPathResource("static/images/alc-logo.png");
//            helper.addInline("logo", logo);

            helper.setFrom("noreply@artlawcommunion.com");
            helper.setTo(contactDto.getEmail());
            helper.setSubject("Thank You for Contacting Art Law Communion");
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send contact confirmation", e);
        }
    }

    @Override
    public void sendContactNotification(ContactRequest contactRequest) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", contactRequest.getName());
            context.setVariable("email", contactRequest.getEmail());
            context.setVariable("subject", contactRequest.getSubject());
            context.setVariable("message", contactRequest.getMessage());
            context.setVariable("attachmentUrl", contactRequest.getAttachmentUrl());
            context.setVariable("createdAt", contactRequest.getCreatedAt());
            String htmlContent = templateEngine.process("contact-notification", context);

            // Attach logo
//            Resource logo = new ClassPathResource("static/images/alc-logo.png");
//            helper.addInline("logo", logo);

            helper.setFrom("noreply@artlawcommunion.com");
            helper.setTo("artlawcommunion@gmail.com");
            helper.setSubject("New Contact Request: " + contactRequest.getSubject());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send contact notification", e);
        }
    }
}
