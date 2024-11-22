package com.locadora.locadoraLivro.Rents.DTOs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record RentResponseDTO(
        int id,
        RenterModel renter,
        BookModel book,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate deadLine,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate devolutionDate,
        @JsonFormat(pattern = "yyyy-MM-dd") LocalDate rentDate,
        RentStatusEnum status
) {
}