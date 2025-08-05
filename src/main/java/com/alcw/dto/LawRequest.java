package com.alcw.dto;

import jakarta.validation.constraints.NotBlank;

public class LawRequest {
    @NotBlank(message = "Question cannot be blank")
    private String question;

    // Getters and setters
    public String getQuestion() { return question; }
    public void setQuestion(String question) { this.question = question; }
}
