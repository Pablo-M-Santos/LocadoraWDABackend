package com.locadora.locadoraLivro.Renters.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import org.springframework.stereotype.Component;

@Component
public class RenterEmailValidation {

    private final RenterRepository renterRepository;

    public RenterEmailValidation(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    public void validateEmail(String email) {
        if (email != null && renterRepository.findByEmail(email) != null) {
            throw new CustomValidationException("Email already in use.");
        }
    }
}
