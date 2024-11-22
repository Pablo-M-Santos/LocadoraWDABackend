package com.locadora.locadoraLivro.Renters.Validation;

import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.DTOs.CreateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Renters.services.RenterServices;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RenterValidationTest {

    @Mock
    private RenterServices renterServices;

    @Mock
    private RenterRepository renterRepository;

    @InjectMocks
    private RenterValidation renterValidation;

    private CreateRenterRequestDTO createRenterRequestDTO;
    private UpdateRenterRequestDTO updateRenterRequestDTO;
    private RenterModel existingRenter;

    @BeforeEach
    void setUp() {
        createRenterRequestDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        updateRenterRequestDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        existingRenter = new RenterModel("Existing Renter", "existing@example.com", "(12) 34567-1234", "Address", "12345678900");
    }

    // Criar Nome nulo ou vazio
    @Test
    void shouldThrowExceptionWhenCreatingWithNullOrEmptyName() {
        CreateRenterRequestDTO nullNameDTO = new CreateRenterRequestDTO(null, "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(nullNameDTO), "O nome de locatário não pode estar vazio.");

        CreateRenterRequestDTO emptyNameDTO = new CreateRenterRequestDTO("", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(emptyNameDTO), "O nome de locatário não pode estar vazio.");
    }


    // Criar Email nulo ou vazio
    @Test
    void shouldThrowExceptionWhenCreatingWithNullOrEmptyEmail() {
        CreateRenterRequestDTO nullEmailDTO = new CreateRenterRequestDTO("John Doe", null, "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(nullEmailDTO), "O e-mail não pode estar vazio.");

        CreateRenterRequestDTO emptyEmailDTO = new CreateRenterRequestDTO("John Doe", "", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(emptyEmailDTO), "O e-mail não pode estar vazio.");
    }

    // Criar Email inválido
    @Test
    void shouldThrowExceptionWhenCreatingWithInvalidEmail() {
        CreateRenterRequestDTO invalidEmailDTO = new CreateRenterRequestDTO("John Doe", "invalid-email", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(invalidEmailDTO), "Formato de e-mail inválido.");
    }

    // Criar Telefone vazio ou nulo
    @Test
    void shouldThrowExceptionWhenCreatingWithNullOrEmptyTelefone() {
        CreateRenterRequestDTO nullTelefoneDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", null, "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(nullTelefoneDTO), "O telefone não pode estar vazio.");

        CreateRenterRequestDTO emptyTelefoneDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(emptyTelefoneDTO), "O telefone não pode estar vazio.");
    }

    // Criar Telefone inválido
    @Test
    void shouldThrowExceptionWhenCreatingWithInvalidTelefone() {
        CreateRenterRequestDTO invalidTelefoneDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "123", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(invalidTelefoneDTO), "Telefone inválido.");
    }

    // Criar Endereço vazio ou nulo
    @Test
    void shouldThrowExceptionWhenCreatingWithNullOrEmptyAddress() {
        CreateRenterRequestDTO nullAddressDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", null, "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(nullAddressDTO), "O endereço não pode estar vazio.");

        CreateRenterRequestDTO emptyAddressDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(emptyAddressDTO), "O endereço não pode estar vazio.");
    }

    // Criar CPF vazio ou nulo
    @Test
    void shouldThrowExceptionWhenCreatingWithNullOrEmptyCpf() {
        CreateRenterRequestDTO nullCpfDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", null);
        assertThrows(CustomValidationException.class, () -> renterValidation.create(nullCpfDTO), "O CPF não pode estar vazio.");

        CreateRenterRequestDTO emptyCpfDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(emptyCpfDTO), "O CPF não pode estar vazio.");
    }

    // Criar CPF inválido
    @Test
    void shouldThrowExceptionWhenCreatingWithInvalidCpf() {
        CreateRenterRequestDTO invalidCpfDTO = new CreateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678901");
        assertThrows(CustomValidationException.class, () -> renterValidation.create(invalidCpfDTO), "CPF inválido.");
    }

    // Criar CPF em uso
    @Test
    void shouldThrowExceptionWhenCreatingWithCpfInUse() {
        lenient().when(renterRepository.findByCpf(createRenterRequestDTO.cpf())).thenReturn(existingRenter);
        assertThrows(CustomValidationException.class, () -> renterValidation.create(createRenterRequestDTO), "O CPF já está em uso.");
    }


    // Atualizar nome nulo ou vazio
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullOrEmptyName() {
        UpdateRenterRequestDTO nullNameDTO = new UpdateRenterRequestDTO(null, "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(nullNameDTO, existingRenter.getId()), "O nome de locatário não pode estar vazio.");

        UpdateRenterRequestDTO emptyNameDTO = new UpdateRenterRequestDTO("", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(emptyNameDTO, existingRenter.getId()), "O nome de locatário não pode estar vazio.");
    }

    // Atualizar Email em uso
    @Test
    void shouldThrowExceptionWhenUpdatingWithEmailInUse() {
        when(renterRepository.findById(existingRenter.getId())).thenReturn(Optional.of(existingRenter));

        when(renterRepository.findByEmailAndIsDeletedFalse(updateRenterRequestDTO.email())).thenReturn(new RenterModel());

        CustomValidationException exception = assertThrows(CustomValidationException.class, () -> {
            renterValidation.update(updateRenterRequestDTO, existingRenter.getId());
        });

        assertEquals("E-mail já em uso.", exception.getMessage());
    }


    // Atualizar Email nulo ou vazio
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullOrEmptyEmail() {
        UpdateRenterRequestDTO nullEmailDTO = new UpdateRenterRequestDTO("John Doe", null, "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(nullEmailDTO, existingRenter.getId()), "O e-mail não pode estar vazio.");

        UpdateRenterRequestDTO emptyEmailDTO = new UpdateRenterRequestDTO("John Doe", "", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(emptyEmailDTO, existingRenter.getId()), "O e-mail não pode estar vazio.");
    }

    // Atualizar Email inválido
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidEmail() {
        UpdateRenterRequestDTO invalidEmailDTO = new UpdateRenterRequestDTO("John Doe", "invalid-email", "(12) 34567-1234", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(invalidEmailDTO, existingRenter.getId()), "Formato de e-mail inválido.");
    }

    // Atualizar Telefone vazio ou nulo
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullOrEmptyTelefone() {
        UpdateRenterRequestDTO nullTelefoneDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", null, "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(nullTelefoneDTO, existingRenter.getId()), "O telefone não pode estar vazio.");

        UpdateRenterRequestDTO emptyTelefoneDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(emptyTelefoneDTO, existingRenter.getId()), "O telefone não pode estar vazio.");
    }

    // Atualizar Telefone inválido
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidTelefone() {
        UpdateRenterRequestDTO invalidTelefoneDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "123", "Address", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(invalidTelefoneDTO, existingRenter.getId()), "Telefone inválido.");
    }

    // Atualizar Endereço vazio ou nulo
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullOrEmptyAddress() {
        UpdateRenterRequestDTO nullAddressDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", null, "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(nullAddressDTO, existingRenter.getId()), "O endereço não pode estar vazio.");

        UpdateRenterRequestDTO emptyAddressDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "", "12345678900");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(emptyAddressDTO, existingRenter.getId()), "O endereço não pode estar vazio.");
    }

    // Atualizar CPF vazio ou nulo
    @Test
    void shouldThrowExceptionWhenUpdatingWithNullOrEmptyCpf() {
        UpdateRenterRequestDTO nullCpfDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", null);
        assertThrows(CustomValidationException.class, () -> renterValidation.update(nullCpfDTO, existingRenter.getId()), "O CPF não pode estar vazio.");

        UpdateRenterRequestDTO emptyCpfDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(emptyCpfDTO, existingRenter.getId()), "O CPF não pode estar vazio.");
    }

    // Atualizar CPF inválido
    @Test
    void shouldThrowExceptionWhenUpdatingWithInvalidCpf() {
        UpdateRenterRequestDTO invalidCpfDTO = new UpdateRenterRequestDTO("John Doe", "john.doe@example.com", "(12) 34567-1234", "Address", "12345678901");
        assertThrows(CustomValidationException.class, () -> renterValidation.update(invalidCpfDTO, existingRenter.getId()), "CPF inválido.");
    }
}
