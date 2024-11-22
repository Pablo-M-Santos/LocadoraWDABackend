package com.locadora.locadoraLivro.Publishers.DTOs;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.URL;

public record UpdatePublisherRequestDTO(
        @NotBlank(message = "The name cannot be empty or contain only spaces.")
        String name,

        @Email(message = "Email must be valid.")
        @NotEmpty(message = "Email cannot be empty.")
        String email,

        @NotBlank(message = "Telephone cannot be empty or contain only spaces.")
        String telephone,

        @URL(message = "URL invalid")
        String site
) {
}
