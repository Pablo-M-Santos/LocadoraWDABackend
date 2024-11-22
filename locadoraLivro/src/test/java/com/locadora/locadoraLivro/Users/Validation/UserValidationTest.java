package com.locadora.locadoraLivro.Users.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import com.locadora.locadoraLivro.Users.services.UserServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserValidationTest {

    @Mock
    private UserServices userServices;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserValidation userValidation;

    private CreateUserRequestDTO createUserRequestDTO;
    private UpdateUserRequestDTO updateUserRequestDTO;
    private UserModel existingUser;


    @BeforeEach
    void setUp() {
        createUserRequestDTO = new CreateUserRequestDTO("John Doe", "john.doe@example.com", "password123", UserRoleEnum.USER);
        updateUserRequestDTO = new UpdateUserRequestDTO("John Doe", "john.doe@example.com", UserRoleEnum.USER);
        existingUser = new UserModel(1, "Existing User", "existing@example.com", "password123", UserRoleEnum.USER);
    }

    // Nome em uso
    @Test
    void shouldThrowExceptionWhenNameIsInUse() {
        when(userRepository.findByName(createUserRequestDTO.name())).thenReturn(existingUser);

        assertThrows(CustomValidationException.class, () -> userValidation.create(createUserRequestDTO));
    }

    // Nome nulo ou vazio
    @Test
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        CreateUserRequestDTO nullNameDTO = new CreateUserRequestDTO(null, "john.doe@example.com", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(nullNameDTO), "O nome de usuário não pode estar vazio.");

        CreateUserRequestDTO emptyNameDTO = new CreateUserRequestDTO("", "john.doe@example.com", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(emptyNameDTO), "O nome de usuário não pode estar vazio.");
    }

    // Email em uso
    @Test
    void shouldThrowExceptionWhenEmailIsInUse() {
        when(userRepository.findByEmail(createUserRequestDTO.email())).thenReturn(existingUser);

        assertThrows(CustomValidationException.class, () -> userValidation.create(createUserRequestDTO));
    }

    // Email nulo ou vazio
    @Test
    void shouldThrowExceptionWhenEmailIsNullOrEmpty() {
       CreateUserRequestDTO nullEmailDTO = new CreateUserRequestDTO("John Doe", null, "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(nullEmailDTO), "O e-mail não pode estar vazio.");

        CreateUserRequestDTO emptyEmailDTO = new CreateUserRequestDTO("John Doe", "", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(emptyEmailDTO), "O e-mail não pode estar vazio.");
    }

    // Formato inválido do Email
    @Test
    void shouldThrowExceptionWhenEmailFormatIsInvalid() {
        CreateUserRequestDTO invalidEmailDTO = new CreateUserRequestDTO("John Doe", "invalid-email", "password123", UserRoleEnum.USER);
        assertThrows(CustomValidationException.class, () -> userValidation.create(invalidEmailDTO), "Formato de e-mail inválido.");
    }

    // Criação com dados válidos
    @Test
    void shouldCreateWhenValidData() {
        when(userRepository.findByName(createUserRequestDTO.name())).thenReturn(null);
        when(userRepository.findByEmail(createUserRequestDTO.email())).thenReturn(null);

        userValidation.create(createUserRequestDTO);
    }

    // Atualizar com nome existente
    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingName() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByName(updateUserRequestDTO.name())).thenReturn(new UserModel());

        assertThrows(CustomValidationException.class, () -> userValidation.update(updateUserRequestDTO, existingUser.getId()));
    }

    // Atualizar com email existente
    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByEmail(updateUserRequestDTO.email())).thenReturn(new UserModel());

        assertThrows(CustomValidationException.class, () -> userValidation.update(updateUserRequestDTO, existingUser.getId()));
    }

    // Atualiza com dados válidos
    @Test
    void shouldUpdateWhenValidData() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        when(userRepository.findByName(updateUserRequestDTO.name())).thenReturn(null);
        when(userRepository.findByEmail(updateUserRequestDTO.email())).thenReturn(null);

        userValidation.update(updateUserRequestDTO, existingUser.getId());
    }

    // Atualização sem modificações
    @Test
    void shouldNotThrowExceptionWhenNameAndEmailAreUnchanged() {
        when(userRepository.findById(existingUser.getId())).thenReturn(Optional.of(existingUser));
        userValidation.update(updateUserRequestDTO, existingUser.getId());
    }

}