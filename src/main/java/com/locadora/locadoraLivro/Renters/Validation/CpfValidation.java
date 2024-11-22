package com.locadora.locadoraLivro.Renters.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import org.springframework.stereotype.Component;

@Component
public class CpfValidation {

    private final RenterRepository renterRepository;

    public CpfValidation(RenterRepository renterRepository) {
        this.renterRepository = renterRepository;
    }

    public void validateCpf(String cpf) {
        if (cpf != null && renterRepository.findByCpf(cpf) != null) {
            throw new CustomValidationException("CPF already in use.");
        }
    }
}
