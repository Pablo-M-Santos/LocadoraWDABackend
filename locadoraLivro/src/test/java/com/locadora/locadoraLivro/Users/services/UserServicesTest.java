package com.locadora.locadoraLivro.Users.services;

import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.Validation.UserValidation;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import com.locadora.locadoraLivro.Users.repositories.PasswordResetTokenRepository;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServicesTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private UserValidation userValidation;

    @Mock
    private PasswordResetTokenRepository resetTokenRepository;

    @InjectMocks
    private UserServices userServices;

    private CreateUserRequestDTO createUserRequestDTO;
    private UpdateUserRequestDTO updateUserRequestDTO;
    private UserModel userModel;

    @BeforeEach
    void setUp() {
        createUserRequestDTO = new CreateUserRequestDTO("John Doe", "john.doe@example.com", "password123", UserRoleEnum.USER);
        updateUserRequestDTO = new UpdateUserRequestDTO("Jane Doe", "jane.doe@example.com", UserRoleEnum.ADMIN);
        userModel = new UserModel("John Doe", "john.doe@example.com", "encryptedPassword123", UserRoleEnum.USER);
        userModel.setId(1);
    }

    // Criação do usuário
    @Test
    void shouldCreateUser() {
        String encryptedPassword = "encryptedPassword123";
        when(passwordEncoder.encode(createUserRequestDTO.password())).thenReturn(encryptedPassword);

        when(userRepository.save(any(UserModel.class))).thenAnswer(invocation -> {
            UserModel user = invocation.getArgument(0);
            user.setId(1);
            return user;
        });

        ResponseEntity<UserModel> response = userServices.create(createUserRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        verify(userValidation, times(1)).validateName(createUserRequestDTO);
        verify(userValidation, times(1)).validateEmail(createUserRequestDTO);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel savedUser = userCaptor.getValue();
        assertEquals(createUserRequestDTO.name(), savedUser.getName());
        assertEquals(createUserRequestDTO.email(), savedUser.getEmail());
        assertEquals(encryptedPassword, savedUser.getPassword());
        assertEquals(createUserRequestDTO.role(), savedUser.getRole());
    }

    // Email inválido
    @Test
    void shouldThrowExceptionWhenEmailIsInvalidOnCreate() {
        doThrow(new IllegalArgumentException("Invalid email")).when(userValidation).validateEmail(createUserRequestDTO);
        Exception exception = assertThrows(IllegalArgumentException.class, () -> userServices.create(createUserRequestDTO));
        assertEquals("Invalid email", exception.getMessage());
    }

    // Atualização do usuário
    @Test
    void shouldUpdateUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userModel));
        when(userRepository.save(any(UserModel.class))).thenReturn(userModel);

        ResponseEntity<Object> response = userServices.update(1, updateUserRequestDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        verify(userValidation, times(1)).validateNameUpdate(updateUserRequestDTO, 1);
        verify(userValidation, times(1)).validateUpdateEmail(updateUserRequestDTO, 1);

        ArgumentCaptor<UserModel> userCaptor = ArgumentCaptor.forClass(UserModel.class);
        verify(userRepository, times(1)).save(userCaptor.capture());

        UserModel updatedUser = userCaptor.getValue();
        assertEquals("Jane Doe", updatedUser.getName());
        assertEquals("jane.doe@example.com", updatedUser.getEmail());
        assertEquals(UserRoleEnum.ADMIN, updatedUser.getRole());
    }

    // Usuário inexistente
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentUser() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = userServices.update(1, updateUserRequestDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    // Deletar usuário
    @Test
    void shouldDeleteUser() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userModel));
        ResponseEntity<Object> response = userServices.delete(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User deleted successfully", response.getBody());
        verify(userRepository, times(1)).delete(userModel);
    }

    // Deletar usuário inexistente
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentUser() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        ResponseEntity<Object> response = userServices.delete(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found", response.getBody());
    }

    // Busca pelo ID
    @Test
    void shouldGetUserById() {
        when(userRepository.findById(1)).thenReturn(Optional.of(userModel));
        Optional<UserModel> foundUser = userServices.findById(1);
        assertTrue(foundUser.isPresent());
        assertEquals(userModel, foundUser.get());
    }

    // Id não encontrado
    @Test
    void shouldReturnEmptyWhenUserNotFoundById() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());
        Optional<UserModel> foundUser = userServices.findById(1);
        assertFalse(foundUser.isPresent());
    }
}
