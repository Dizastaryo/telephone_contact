package com.example.testfortelephon.repository;

import com.example.testfortelephon.model.Contact;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ContactRepository extends MongoRepository<Contact, String> {
}
