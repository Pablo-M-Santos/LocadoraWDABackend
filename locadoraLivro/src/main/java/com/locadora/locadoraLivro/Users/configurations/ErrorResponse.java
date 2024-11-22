package com.locadora.locadoraLivro.Users.configurations;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorResponse {

    @Schema(description = "Código de status", example = "0")
    private int status;

    @Schema(description = "Data e hora do erro", example = "2000-01-01T00:00:00Z")
    private String timestamp;

    @Schema(description = "URL para detalhes do erro", example = "https://localhost/invalid-data")
    private String type;

    @Schema(description = "Título do erro", example = "Invalid data")
    private String title;

    @Schema(description = "Mensagem do usuário", example = "One or more fields are invalid. Fill in correctly and try again.")
    private String userMessage;

    @Schema(description = "Lista de objetos ou campos que geraram erro", example = "List of invalid fields")
    private String objects;

}
