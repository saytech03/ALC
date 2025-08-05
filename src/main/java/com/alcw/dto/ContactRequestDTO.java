package com.alcw.dto;


import com.alcw.model.ContactSubmission;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ContactRequestDTO {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Subject is required")
    private ContactSubmission.ContactSubject subject;

    @NotBlank(message = "Message is required")
    private String message;

    private MultipartFile blogFile;

    @NotBlank(message = "Captcha token is required")
    private String captchaToken;
}