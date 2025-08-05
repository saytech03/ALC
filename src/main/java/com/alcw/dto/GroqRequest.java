package com.alcw.dto;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GroqRequest {
    private String model;
    private List<Map<String, String>> messages;
    private double temperature;

    // Constructor
    public GroqRequest(String model, String systemPrompt, String userQuestion) {
        this.model = model;
        this.messages = Arrays.asList(
                Map.of("role", "system", "content", systemPrompt),
                Map.of("role", "user", "content", userQuestion)
        );
        this.temperature = 0.7;
    }

    // Getters
    public String getModel() { return model; }
    public List<Map<String, String>> getMessages() { return messages; }
    public double getTemperature() { return temperature; }
}
