package com.alcw.service;


public interface GoogleSheetsService {
    void writeToSheet(String name, String email, String subject, String message, String fileUrl);
}
