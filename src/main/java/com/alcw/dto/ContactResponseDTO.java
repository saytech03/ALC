package com.alcw.dto;


import lombok.Data;

@Data
public class ContactResponseDTO {
    private String name;
    private String email;
    private String subject;
    private String message;
    private String fileUrl;
    private String status;
}
