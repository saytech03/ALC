package com.alcw.dto;

import java.util.List;

public class AIResponse {
    private List<Choice> choices;

    // Getters
    public List<Choice> getChoices() { return choices; }

    public static class Choice {
        private Message message;

        // Getters
        public Message getMessage() { return message; }

        public static class Message {
            private String content;

            // Getters
            public String getContent() { return content; }
        }
    }
}