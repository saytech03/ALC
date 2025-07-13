package com.alcw.service;

import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactRequest;
import com.alcw.repository.ContactRequestRepository;
import com.cloudinary.Cloudinary;
import com.alcw.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService {

    private final ContactRequestRepository contactRequestRepository;
    private final Cloudinary cloudinary;
    private final EmailService emailService;

    @Override
    public ContactRequest processContactRequest(ContactRequestDTO contactDto) {
        // Handle file upload
        String attachmentUrl = null;
        if (contactDto.getBlogFile() != null && !contactDto.getBlogFile().isEmpty()) {
            attachmentUrl = uploadFile(contactDto.getBlogFile());
        }

        // Save to database
        ContactRequest contactRequest = new ContactRequest();
        contactRequest.setName(contactDto.getName());
        contactRequest.setEmail(contactDto.getEmail());
        contactRequest.setSubject(contactDto.getSubject());
        contactRequest.setMessage(contactDto.getMessage());
        contactRequest.setAttachmentUrl(attachmentUrl);
        ContactRequest savedRequest = contactRequestRepository.save(contactRequest);

        // Send emails
        emailService.sendContactConfirmation(contactDto);
        emailService.sendContactNotification(savedRequest);

        return savedRequest;
    }

    private String uploadFile(MultipartFile file) {
        try {
            Map<?, ?> uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "alc_contact_attachments")
            );
            return (String) uploadResult.get("secure_url");
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
