package com.locadora.locadoraLivro.Publishers.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import org.springframework.stereotype.Component;

@Component
public class PublisherEmailValidation {

    private final PublisherRepository publisherRepository;

    public PublisherEmailValidation(PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    public void validateEmail(String email) {
        if (email != null && publisherRepository.findByEmail(email) != null) {
            throw new CustomValidationException("E-mail já em uso");
        }
    }
}
