package com.locadora.locadoraLivro.Users.DTOs;


import com.locadora.locadoraLivro.Users.models.UserRoleEnum;

public record LoginResponseDTO(String token,String name, String email, UserRoleEnum role) {

}
