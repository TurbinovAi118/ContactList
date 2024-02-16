package com.example.demo.service;

import com.example.demo.model.Contact;

import java.util.List;
import java.util.Optional;

public interface ContactService {

    List<Contact> findAll();

    Contact findById(Long id);

    Contact save(Contact contact);

    Contact update(Contact contact);

    void deleteById(Long id);

}
