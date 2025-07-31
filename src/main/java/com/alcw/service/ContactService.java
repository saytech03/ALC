package com.alcw.service;


import com.alcw.dto.ContactRequestDTO;
import com.alcw.dto.ContactResponseDTO;

public interface ContactService {
    ContactResponseDTO processContactRequest(ContactRequestDTO request);
}
