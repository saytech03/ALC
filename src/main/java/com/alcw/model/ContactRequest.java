package com.alcw.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "contact_requests")
@Data
public class ContactRequest {
    @Id
    private String id;
    private String name;
    private String email;
    private String subject;
    private String message;
    private String attachmentUrl;
    private LocalDateTime createdAt = LocalDateTime.now();
}