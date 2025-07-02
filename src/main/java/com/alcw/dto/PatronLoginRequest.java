package com.alcw.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PatronLoginRequest {
    @NotBlank(message = "Patron ID is required")
    private String patronId;

    @NotBlank(message = "Password is required")
    private String password;
}
