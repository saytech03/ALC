package com.alcw.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String id;
    private String name;
    private String email;
    private String occupation;
    private String message;
    private String token;
    private String membershipId;
    private String profileImageUrl;

    public AuthResponse(String id, String name, String email, String occupation, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.message = message;
    }

    public AuthResponse(String id, String name, String email, String occupation, String token, String membershipId) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.occupation = occupation;
        this.token = token;
        this.membershipId = membershipId;
    }
}
