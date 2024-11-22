package com.locadora.locadoraLivro.Users.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@AllArgsConstructor
@Component
public class UserValidation {

    private final UserRepository userRepository;

    public void create(CreateUserRequestDTO data) {
        validateName(data);
        validateEmail(data);
    }

    public void update(UpdateUserRequestDTO data, int id) {
        validateNameUpdate(data, id);
        validateUpdateEmail(data, id);
    }

    private void validateName(CreateUserRequestDTO data) {
        if (data.name() == "" || data.name() == null) {
            throw new CustomValidationException("O nome de usuário não pode estar vazio.");
        }

        if (userRepository.findByName(data.name()) != null) {
            throw new CustomValidationException("Nome do usuário em uso");
        }
    }

    private void validateNameUpdate(UpdateUserRequestDTO data, int id) {
        UserModel userModel = userRepository.findById(id).get();

        if (data.name() == "" || data.name() == null) {
            throw new CustomValidationException("O nome de usuário não pode estar vazio.");
        }

        if (!Objects.equals(userModel.getName(), data.name())) {
            if (userRepository.findByName(data.name()) != null) {
                throw new CustomValidationException("Nome do usuário em uso");
            }
        }
    }

    private void validateEmail(CreateUserRequestDTO data) {
        if (data.email() == "" || data.email() == null) {
            throw new CustomValidationException("O email não pode estar vazio.");
        }

        if (userRepository.findByEmail(data.email()) != null) {
            throw new CustomValidationException("Email já em uso.");
        }
    }

    private void validateUpdateEmail(UpdateUserRequestDTO data, int id) {
        UserModel userModel = userRepository.findById(id).get();

        if (data.email() == "" || data.email() == null) {
            throw new CustomValidationException("O email não pode estar vazio.");
        }

        if (!Objects.equals(userModel.getEmail(), data.email())) {
            if (userRepository.findByEmail(data.email()) != null) {
                throw new CustomValidationException("Email já em uso.");
            }
        }
    }
}