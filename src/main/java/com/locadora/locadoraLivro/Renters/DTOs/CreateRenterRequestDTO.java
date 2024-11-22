package com.locadora.locadoraLivro.Renters.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.br.CPF;

public record CreateRenterRequestDTO(
        @NotBlank(message = "The name cannot be empty or contain only spaces.")
        String name,

        @Email(message = "Email must be valid.")
        @NotEmpty(message = "Email cannot be empty.")
        String email,

        @NotBlank(message = "Telephone cannot be empty or contain only spaces.")
        String telephone,

        @NotBlank(message = "Address cannot be empty or contain only spaces.")
        String address,

        @CPF(message = "Invalid CPF format.")
        String cpf
) {
}
