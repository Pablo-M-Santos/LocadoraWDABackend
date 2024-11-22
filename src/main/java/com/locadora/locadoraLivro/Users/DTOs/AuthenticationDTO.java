package com.locadora.locadoraLivro.Users.DTOs;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthenticationDTO {
    private String email;
    private String password;
}
