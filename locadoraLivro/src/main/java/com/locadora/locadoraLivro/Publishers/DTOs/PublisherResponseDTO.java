package com.locadora.locadoraLivro.Publishers.DTOs;

import lombok.Builder;

@Builder
public record PublisherResponseDTO(
        int id,
        String name,
        String email,
        String telephone,
        String site
) {
}
