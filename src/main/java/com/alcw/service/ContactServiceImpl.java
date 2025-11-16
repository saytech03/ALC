package com.alcw.service;

import com.alcw.dto.ContactRequestDTO;
import com.alcw.dto.ContactResponseDTO;
import com.alcw.model.ContactSubmission;
import com.alcw.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final CloudinaryService cloudinaryService;
    private final EmailService emailService;
    private final GoogleSheetsService googleSheetsService;
    private final ContactRepository contactRepository;

    @Value("${spring.mail.username}")
    private String adminEmail;

    @Override
    public ContactResponseDTO processContactRequest(ContactRequestDTO request) {
        // CAPTCHA verification removed

        // Upload file if exists
        String fileUrl = null;
        if (request.getBlogFile() != null && !request.getBlogFile().isEmpty()) {
            fileUrl = cloudinaryService.uploadFile(request.getBlogFile());
        }

        // Save to database
        ContactSubmission submission = new ContactSubmission();
        submission.setName(request.getName());
        submission.setEmail(request.getEmail());
        submission.setSubject(request.getSubject());
        submission.setMessage(request.getMessage());
        submission.setFileUrl(fileUrl);
        contactRepository.save(submission);

        // Write to Google Sheets
        googleSheetsService.writeToSheet(request.getName(), request.getEmail(),
                request.getSubject().name(), request.getMessage(), fileUrl);

        // Handle notifications based on subject
        ContactResponseDTO response = new ContactResponseDTO();
        response.setName(request.getName());
        response.setEmail(request.getEmail());
        response.setSubject(request.getSubject().name());
        response.setMessage(request.getMessage());
        response.setFileUrl(fileUrl);

        switch (request.getSubject()) {
            case BLOG_SUBMISSION:
            case COLLABORATION:
                emailService.sendUserConfirmation(request.getEmail(), request.getName(),
                        request.getSubject(), fileUrl);
                emailService.sendAdminNotification(adminEmail, request.getName(),
                        request.getEmail(), request.getSubject(), request.getMessage(), fileUrl);
                response.setStatus("Thank you! We've received your submission and sent a confirmation email.");
                break;
            case REMARKS:
            case OTHERS:
                response.setStatus("Thank you for your feedback!");
                break;
        }

        return response;
    }
}