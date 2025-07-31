package com.alcw.controller;

import com.alcw.dto.ContactRequestDTO;
import com.alcw.dto.ContactResponseDTO;
import com.alcw.service.ContactService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactResponseDTO> contactUs(@Valid @ModelAttribute ContactRequestDTO request) {
        ContactResponseDTO response = contactService.processContactRequest(request);
        return ResponseEntity.ok(response);
    }
}
