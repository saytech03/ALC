package com.alcw.repository;

import com.alcw.model.ContactSubmission;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<ContactSubmission, String> {
}
