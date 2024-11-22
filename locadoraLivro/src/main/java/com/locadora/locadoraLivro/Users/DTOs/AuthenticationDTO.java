package com.locadora.locadoraLivro.Users.DTOs;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthenticationDTO(
        @Schema(description = "email", example = "email", required = true)
        String email,

        @Schema(description = "password", example = "password", required = true)
        String password) {
}