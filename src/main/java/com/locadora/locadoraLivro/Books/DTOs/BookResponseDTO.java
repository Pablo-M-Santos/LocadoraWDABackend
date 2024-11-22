package com.locadora.locadoraLivro.Books.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record BookResponseDTO(
        int id,
        String name,
        String author,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate launchDate,
        int totalQuantity,
        int totalInUse,
        PublisherModel publisher
) {
}
