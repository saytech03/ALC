package com.alcw.service;

import com.alcw.dto.ContactRequestDTO;
import com.alcw.model.ContactRequest;

public interface ContactService {
    ContactRequest processContactRequest(ContactRequestDTO contactDto);
}
