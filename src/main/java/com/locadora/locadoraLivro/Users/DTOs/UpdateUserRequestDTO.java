package com.locadora.locadoraLivro.Users.DTOs;

import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateUserRequestDTO(
        @NotBlank(message = "The name cannot be empty or contain only spaces.")
        String name,

        @Email(message = "Email must be valid.")
        @NotBlank(message = "Email cannot be empty or contain only spaces.")
        String email,

        String password,

        @NotNull(message = "Access level cannot be null.")
        UserRoleEnum role
){
}
