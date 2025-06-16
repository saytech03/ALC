package com.alcw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileUpdateResponse {
    private String id;
    private String name;
    private String email;
    private String occupation;
    private String profileImageUrl;
    private String message;
}
