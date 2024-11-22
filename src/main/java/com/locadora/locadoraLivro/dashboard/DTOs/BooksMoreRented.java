package com.locadora.locadoraLivro.dashboard.DTOs;

import lombok.Builder;

@Builder
public record BooksMoreRented(
        String name,
        int totalRents){
}
