package com.locadora.locadoraLivro.Rents.Validation;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Rents.DTOs.CreateRentRequestDTO;
import com.locadora.locadoraLivro.Rents.DTOs.UpdateRentRecordDTO;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RentValidationTest {

    @InjectMocks
    private RentValidation rentValidation;

    @Mock
    private RenterRepository renterRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RentRepository rentRepository;

    private RenterModel renter;
    private BookModel book;
    private CreateRentRequestDTO createRentRequestDTO;
    private UpdateRentRecordDTO updateRentRecordDTO;

    @BeforeEach
    public void setUp() {
        renter = new RenterModel();
        book = new BookModel();
        createRentRequestDTO = new CreateRentRequestDTO(1, 1, LocalDate.now().plusDays(5));
        updateRentRecordDTO = new UpdateRentRecordDTO(1, 1, LocalDate.now().plusDays(5), LocalDate.now().plusDays(10));

    }

    // Testa a validação do ID do locatário quando o locatário não é encontrado.
    @Test
    public void testValidateRenterIdRenterNotFound() {
        when(renterRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.validateRenterId(createRentRequestDTO));
    }

    // Testa a validação do ID do locatário quando o locatário está marcado como excluído.
    @Test
    public void testValidateRenterIdRenterIsDeleted() {
        renter.setDeleted(true);
        when(renterRepository.findById(1)).thenReturn(Optional.of(renter));

        assertThrows(CustomValidationException.class, () -> rentValidation.validateRenterId(createRentRequestDTO));
    }

    // Testa a validação do ID do locatário quando o locatário é válido e não está excluído.
    @Test
    public void testValidateRenterIdValidRenter() {
        renter.setDeleted(false);
        when(renterRepository.findById(1)).thenReturn(Optional.of(renter));

        // Este teste deve passar sem exceção
        rentValidation.validateRenterId(createRentRequestDTO);
    }

    // Testa a validação do ID do livro quando o livro não é encontrado.
    @Test
    public void testValidateBookIdBookNotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.validateBookId(createRentRequestDTO));
    }

    // Testa a validação do ID do livro quando o livro é válido.
    @Test
    public void testValidateBookIdValidBook() {
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        // Este teste deve passar sem exceção
        rentValidation.validateBookId(createRentRequestDTO);
    }

    // Testa a validação do prazo quando é válido.
    @Test
    public void testValidateDeadLineValidDeadline() {
        // Sem exceção deve ser lançada
        rentValidation.validateDeadLine(createRentRequestDTO);
    }

    // Testa a validação do prazo quando a data está no passado.
    @Test
    public void testValidateDeadLineDeadlineInPast() {
        CreateRentRequestDTO createRentRequestDTO = new CreateRentRequestDTO(1, 1, LocalDate.now().minusDays(1));

        assertThrows(CustomValidationException.class, () -> rentValidation.validateDeadLine(createRentRequestDTO));
    }

    // Testa a validação do prazo quando a data é hoje.
    @Test
    public void testValidateDeadLineDeadlineToday() {
        CreateRentRequestDTO createRentRequestDTO = new CreateRentRequestDTO(1, 1, LocalDate.now());

        rentValidation.validateDeadLine(createRentRequestDTO);
    }

    // Testa a validação para verificar se o locatário já alugou o livro.
    @Test
    public void testValidateRentRepeatedRentExists() {
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(1, 1, RentStatusEnum.ALUGADO)).thenReturn(true);

        assertThrows(CustomValidationException.class, () -> rentValidation.validateRentRepeated(createRentRequestDTO));
    }

    // Testa a validação para o caso onde não há aluguel existente do locatário para o livro.
    @Test
    public void testValidateRentRepeatedNoExistingRent() {
        when(rentRepository.existsByRenterIdAndBookIdAndStatus(1, 1, RentStatusEnum.ALUGADO)).thenReturn(false);

        // Este teste deve passar sem exceção
        rentValidation.validateRentRepeated(createRentRequestDTO);
    }

    // Testa a validação para verificar se o aluguel já foi devolvido.
    @Test
    public void testDeliveredValidateRentAlreadyDelivered() {
        RentModel rent = new RentModel();
        rent.setStatus(RentStatusEnum.ENTREGUE);
        when(rentRepository.findById(1)).thenReturn(Optional.of(rent));

        assertThrows(CustomValidationException.class, () -> rentValidation.deliveredValidate(1));
    }

    // Testa a validação para o caso onde o aluguel ainda não foi devolvido.
    @Test
    public void testDeliveredValidateRentNotDelivered() {
        RentModel rent = new RentModel();
        rent.setStatus(RentStatusEnum.ALUGADO);
        when(rentRepository.findById(1)).thenReturn(Optional.of(rent));

        // Este teste deve passar sem exceção
        rentValidation.deliveredValidate(1);
    }

    // Testa a validação quando o aluguel não existe.
    @Test
    public void testDeliveredValidateRentDoesNotExist() {
        int nonExistentRentId = 999;

        when(rentRepository.findById(nonExistentRentId)).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> rentValidation.deliveredValidate(nonExistentRentId));
    }

}
