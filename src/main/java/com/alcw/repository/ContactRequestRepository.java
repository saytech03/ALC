package com.alcw.repository;

import com.alcw.model.ContactRequest;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRequestRepository extends MongoRepository<ContactRequest, String> {
}
