package com.locadora.locadoraLivro.Users.controllers;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.locadora.locadoraLivro.Users.models.EmailRequest;
import com.locadora.locadoraLivro.Users.models.PasswordResetRequest;
import com.locadora.locadoraLivro.Users.models.TokenValidationRequest;
import com.locadora.locadoraLivro.Users.services.EmailService;
import com.locadora.locadoraLivro.Users.services.UserServices;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class PasswordResetControllerTest {

    @Mock
    private UserServices userServices;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private PasswordResetController passwordResetController;

    @Test
    public void testProcessForgotPasswordSuccess() {
        String email = "usuario@exemplo.com";
        String token = "valid-token";
        String userName = "UsuarioTeste";

        EmailRequest emailRequest = new EmailRequest();
        emailRequest.setEmail(email);

        when(userServices.createPasswordResetToken(email)).thenReturn(token);
        when(userServices.getUserNameByEmail(email)).thenReturn(userName);

        ResponseEntity<String> response = passwordResetController.processForgotPassword(emailRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Instruções de redefinição de senha enviadas para " + email, response.getBody());
    }


    @Test
    public void testResetPasswordSuccess() {
        String token = "valid-token";
        String newPassword = "new-password";

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setToken(token);
        passwordResetRequest.setNewPassword(newPassword);

        when(userServices.resetPassword(token, newPassword)).thenReturn(true);

        ResponseEntity<String> response = passwordResetController.resetPassword(passwordResetRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Senha redefinida com sucesso.", response.getBody());
    }


    @Test
    public void testResetPasswordTokenInvalid() {
        String token = "invalid-token";
        String newPassword = "new-password";

        PasswordResetRequest passwordResetRequest = new PasswordResetRequest();
        passwordResetRequest.setToken(token);
        passwordResetRequest.setNewPassword(newPassword);


        when(userServices.resetPassword(token, newPassword)).thenReturn(false);

        ResponseEntity<String> response = passwordResetController.resetPassword(passwordResetRequest);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Falha ao redefinir a senha. Token inválido ou expirado.", response.getBody());
    }


    @Test
    public void testValidateResetToken() {
        String token = "valid-token";

        TokenValidationRequest tokenRequest = new TokenValidationRequest();
        tokenRequest.setToken(token);

        when(userServices.validatePasswordResetToken(token)).thenReturn(true);

        ResponseEntity<String> response = passwordResetController.validateResetToken(tokenRequest);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Token válido.", response.getBody());
    }
}
