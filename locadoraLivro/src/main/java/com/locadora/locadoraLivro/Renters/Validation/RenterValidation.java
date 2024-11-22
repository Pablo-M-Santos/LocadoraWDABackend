package com.locadora.locadoraLivro.Renters.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.DTOs.CreateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.hibernate.validator.internal.constraintvalidators.hv.br.CPFValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class RenterValidation {

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RentRepository rentRepository;

    public void create(CreateRenterRequestDTO data) {
        validateName(data);
        validateEmail(data);
        validateCPF(data);
    }

    public void update(UpdateRenterRequestDTO data, int id) {
        validateUpdateName(data);
        validateUpdateEmail(data, id);
        validateCPFUpdate(data, id);
    }

    private void validateName(CreateRenterRequestDTO data) {
        if (data.name() == "" || data.name() == null) {
            throw new CustomValidationException("O nome não pode estar vazio.");
        }
        ;
    }

    private void validateUpdateName(UpdateRenterRequestDTO data) {
        if (data.name() == "" || data.name() == null) {
            throw new CustomValidationException("O nome não pode estar vazio.");
        }
        ;
    }

    private void validateEmail(CreateRenterRequestDTO data) {
        if (data.email() == "" || data.email() == null) {
            throw new CustomValidationException("O email não pode estar vazio.");
        }
        ;

        if (renterRepository.findByEmailAndIsDeletedFalse(data.email()) != null) {
            throw new CustomValidationException("Este email já está em uso.");
        }
    }

    private void validateUpdateEmail(UpdateRenterRequestDTO data, int id) {
        RenterModel renter = renterRepository.findById(id).get();

        if (!Objects.equals(renter.getEmail(), data.email())) {
            if (renterRepository.findByEmailAndIsDeletedFalse(data.email()) != null) {
                throw new CustomValidationException("E-mail já em uso.");
            }
        }
    }

    private void validateCPF(CreateRenterRequestDTO data) {
        if (data.cpf() != null && !data.cpf().isBlank()) {
            CPFValidator cpfValidator = new CPFValidator();
            cpfValidator.initialize(null);

            if (!cpfValidator.isValid(data.cpf(), null)) {
                throw new CustomValidationException("Formato de CPF inválido.");
            }

            if (renterRepository.findByCpfAndIsDeletedFalse(data.cpf()) != null) {
                throw new CustomValidationException("CPF já em uso.");
            }
        }
    }

    private void validateCPFUpdate(UpdateRenterRequestDTO data, int id) {
        RenterModel renter = renterRepository.findById(id).get();

        if (data.cpf() != null && !data.cpf().isBlank()) {
            if (!Objects.equals(renter.getCpf(), data.cpf())) {
                CPFValidator cpfValidator = new CPFValidator();
                cpfValidator.initialize(null);

                if (!cpfValidator.isValid(data.cpf(), null)) {
                    throw new CustomValidationException("Formato de CPF inválido.");
                }

                if (renterRepository.findByCpfAndIsDeletedFalse(data.cpf()) != null) {
                    throw new CustomValidationException("CPF já em uso.");
                }
            }
        }
    }

    public void validateDeleteRenter(int id) {
        if (rentRepository.existsByRenterIdAndStatus(id, RentStatusEnum.RENTED)) {
            throw new CustomValidationException("Não é possível excluir o locatário. Existem livros atualmente alugados.");
        }
    }
}