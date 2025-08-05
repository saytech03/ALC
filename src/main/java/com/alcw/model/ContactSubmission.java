package com.alcw.model;



import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "contact_submissions")
@Data
public class ContactSubmission {
    @Id
    private String id;
    private String name;
    private String email;
    private ContactSubject subject;
    private String message;
    private String fileUrl;
    private LocalDateTime submittedAt = LocalDateTime.now();

    public enum ContactSubject {
        BLOG_SUBMISSION, COLLABORATION, REMARKS, OTHERS
    }
}