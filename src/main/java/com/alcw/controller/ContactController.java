package com.alcw.controller;

import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactRequest;
import com.alcw.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ContactRequest> submitContactRequest(@Valid ContactRequestDTO contactDto) {
        ContactRequest result = contactService.processContactRequest(contactDto);
        return ResponseEntity.ok(result);
    }
}
