package com.alcw.service;


import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactSubmission;
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

    // Add these methods to existing implementation
    @Override
    public void sendUserConfirmation(String email, String name,
                                     ContactSubmission.ContactSubject subject, String fileUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("subject", subject.name());
            String htmlContent = templateEngine.process("contact-user-email", context);

            helper.setFrom("noreplyalcwb@gmail.com");
            helper.setTo(email);
            helper.setSubject("Thank you for your " + subject.name().toLowerCase());
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send confirmation email", e);
        }
    }

    @Override
    public void sendAdminNotification(String adminEmail, String name, String userEmail,
                                      ContactSubmission.ContactSubject subject, String messageContent, String fileUrl) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            Context context = new Context();
            context.setVariable("name", name);
            context.setVariable("email", userEmail);
            context.setVariable("subject", subject.name());
            context.setVariable("message", messageContent);
            context.setVariable("fileUrl", fileUrl);
            String htmlContent = templateEngine.process("contact-admin-email", context);

            helper.setFrom("noreplyalcwb@gmail.com");
            helper.setTo(adminEmail);
            helper.setSubject("New " + subject.name().toLowerCase() + " from " + name);
            helper.setText(htmlContent, true);

            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send admin notification", e);
        }
    }
}
