package com.locadora.locadoraLivro.Renters.DTOs;

import lombok.Builder;

@Builder
public record RenterResponseDTO(
        int id,
        String name,
        String email,
        String telephone,
        String address,
        String cpf
){
}
