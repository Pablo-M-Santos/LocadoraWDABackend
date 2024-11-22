package com.locadora.locadoraLivro.dashboard.DTOs;

import lombok.Builder;

@Builder
public record RentsperRenterResponseDTO(
        String name,
        int rentsQuantity,
        int rentsActive) {

}
