package com.locadora.locadoraLivro.Users.controllers;

import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import com.locadora.locadoraLivro.Users.services.TokenService;
import com.locadora.locadoraLivro.Users.DTOs.AuthenticationDTO;
import com.locadora.locadoraLivro.Users.DTOs.LoginResponseDTO;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@SecurityRequirement(name = "Bearer Authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository repository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    @Operation(summary = "Login")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Request successful.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LoginResponseDTO.class),
                            examples = @ExampleObject(value = "{\"token\": \"token\", \"name\": \"username\", \"email\": \"email@gmail.com\", \"role\": \"USER_ROLE\"}"))),
            @ApiResponse(responseCode = "500", description = "There was an internal error.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = "{\"status\": 0, \"timestamp\": \"2000-01-01T00:00:00Z\", \"type\": \"https://localhost/invalid-data\", \"title\": \"Invalid data\", \"detail\": \"One or more fields are invalid. Fill in correctly and try again.\", \"userMessage\": \"One or more fields are invalid. Fill in correctly and try again.\", \"objects\": \"List of objects or fields that generated or error (optional)\"}")))
    })
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        var usernamePassword = new UsernamePasswordAuthenticationToken(data.email(), data.password());
        var auth = this.authenticationManager.authenticate(usernamePassword);

        var token = tokenService.generateToken((UserModel) auth.getPrincipal());

        UserModel user = repository.findByEmail(data.email());
        LoginResponseDTO response = new LoginResponseDTO(token, user.getName(), user.getEmail(), user.getRole());

        return ResponseEntity.ok(response);
    }
}
