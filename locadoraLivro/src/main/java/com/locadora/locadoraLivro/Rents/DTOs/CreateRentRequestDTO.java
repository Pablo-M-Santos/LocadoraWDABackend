package com.locadora.locadoraLivro.Rents.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record CreateRentRequestDTO(
        @NotNull(message = "O campo locatário é obrigatório")
        int renterId,

        @NotNull(message = "O campo livro é obrigatório")
        int bookId,

        @NotNull(message = "O campo data é obrigatório")
        LocalDate deadLine) {
}
