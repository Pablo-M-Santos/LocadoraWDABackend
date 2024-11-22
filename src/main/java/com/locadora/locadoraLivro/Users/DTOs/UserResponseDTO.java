package com.locadora.locadoraLivro.Users.DTOs;

import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import lombok.Builder;

@Builder
public record UserResponseDTO(
        int id,
        String name,
        String email,
        UserRoleEnum role
){
}
