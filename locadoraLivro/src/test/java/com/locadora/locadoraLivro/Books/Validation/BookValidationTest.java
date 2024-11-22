package com.locadora.locadoraLivro.Books.Validation;

import com.locadora.locadoraLivro.Books.DTOs.CreateBookRequestDTO;
import com.locadora.locadoraLivro.Books.DTOs.UpdateBookRecordDTO;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookValidationTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @InjectMocks
    private BookValidation bookValidation;

    private CreateBookRequestDTO createBookRequestDTO;
    private UpdateBookRecordDTO updateBookRecordDTO;


    @BeforeEach
    void setUp() {
        createBookRequestDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name", LocalDate.now(), 5, 1);
        updateBookRecordDTO = new UpdateBookRecordDTO("Updated Title", "Updated Author", LocalDate.now(), 10, 1);
    }

    // Nome nulo ou vazio
    @Test
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        CreateBookRequestDTO nullNameDTO = new CreateBookRequestDTO(null, "Author Name", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(nullNameDTO), "O nome não pode estar vazio ou conter apenas espaços.");

        CreateBookRequestDTO emptyNameDTO = new CreateBookRequestDTO("", "Author Name", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(emptyNameDTO), "O nome não pode estar vazio ou conter apenas espaços.");
    }


    // Nome nulo ou vazio na atualização
    @Test
    void shouldThrowExceptionWhenUpdatingNameIsNullOrEmpty() {
        UpdateBookRecordDTO nullNameDTO = new UpdateBookRecordDTO(null, "Author Name", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(nullNameDTO, 1), "O nome do livro não pode ser vazio ou nulo");

        UpdateBookRecordDTO emptyNameDTO = new UpdateBookRecordDTO("", "Author Name", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(emptyNameDTO, 1), "O nome do livro não pode ser vazio ou nulo");
    }

    // Author nulo ou vazio
    @Test
    void shouldThrowExceptionWhenAuthorIsNullOrEmpty() {
        CreateBookRequestDTO nullAuthorDTO = new CreateBookRequestDTO("Valid Book Title", null, LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(nullAuthorDTO), "O author não pode estar vazio ou conter apenas espaços.");

        CreateBookRequestDTO emptyAuthorDTO = new CreateBookRequestDTO("Valid Book Title", "", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(emptyAuthorDTO), "O author não pode estar vazio ou conter apenas espaços.");
    }

    // Autor nulo ou vazio na atualização
    @Test
    void shouldThrowExceptionWhenUpdatingAuthorIsNullOrEmpty() {
        UpdateBookRecordDTO nullAuthorDTO = new UpdateBookRecordDTO("Valid Book Title", null, LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(nullAuthorDTO, 1), "O autor do livro não pode ser vazio ou nulo");

        UpdateBookRecordDTO emptyAuthorDTO = new UpdateBookRecordDTO("Valid Book Title", "", LocalDate.now(), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(emptyAuthorDTO, 1), "O autor do livro não pode ser vazio ou nulo");
    }

    // Data de lançamento nula
    @Test
    void shouldThrowExceptionWhenLaunchDateIsNull() {
        CreateBookRequestDTO nullLaunchDateDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name", null, 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(nullLaunchDateDTO), "A data de lançamento não pode ser nula");
    }

    // Data de lançamento nula na atualização
    @Test
    void shouldThrowExceptionWhenUpdatingLaunchDateIsNull() {
        UpdateBookRecordDTO nullLaunchDateDTO = new UpdateBookRecordDTO("Valid Book Title", "Author Name", null, 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(nullLaunchDateDTO, 1), "A data de lançamento não pode ser nula");
    }

    // Data de lançamento no futuro
    @Test
    void shouldThrowExceptionWhenLaunchDateIsInFuture() {
        CreateBookRequestDTO futureLaunchDateDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name", LocalDate.now().plusDays(1), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(futureLaunchDateDTO), "A data de lançamento não pode ser no futuro");
    }

    // Data de lançamento no futuro na atualização
    @Test
    void shouldThrowExceptionWhenUpdatingLaunchDateIsInFuture() {
        UpdateBookRecordDTO futureLaunchDateDTO = new UpdateBookRecordDTO("Valid Book Title", "Author Name", LocalDate.now().plusDays(1), 5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(futureLaunchDateDTO, 1), "A data de lançamento não pode ser no futuro");
    }

    // Quantidade total
    @Test
    void shouldThrowExceptionWhenTotalQuantityIsLessThanOne() {
        CreateBookRequestDTO zeroQuantityDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name", LocalDate.now(), 0, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(zeroQuantityDTO), "A quantidade total deve ser maior ou igual a 1");

        CreateBookRequestDTO negativeQuantityDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name", LocalDate.now(), -5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.create(negativeQuantityDTO), "A quantidade total deve ser maior ou igual a 1");
    }

    // Testando totalQuantity na atualização
    @Test
    void shouldThrowExceptionWhenUpdatingTotalQuantityIsLessThanOne() {
        UpdateBookRecordDTO zeroQuantityDTO = new UpdateBookRecordDTO("Valid Book Title", "Author Name", LocalDate.now(), 0, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(zeroQuantityDTO, 1), "A quantidade total deve ser maior ou igual a 1");

        UpdateBookRecordDTO negativeQuantityDTO = new UpdateBookRecordDTO("Valid Book Title", "Author Name", LocalDate.now(), -5, 1);
        assertThrows(CustomValidationException.class, () -> bookValidation.update(negativeQuantityDTO, 1), "A quantidade total deve ser maior ou igual a 1");
    }

    // Testando criação com dados válidos
    @Test
    void shouldCreateBookSuccessfullyWithValidData() {
        PublisherModel mockPublisher = new PublisherModel();
        when(publisherRepository.findById(createBookRequestDTO.publisherId())).thenReturn(Optional.of(mockPublisher));

        assertDoesNotThrow(() -> bookValidation.create(createBookRequestDTO));
    }

    // Testando atualização com dados válidos
    @Test
    void shouldUpdateBookSuccessfullyWithValidData() {
        UpdateBookRecordDTO validUpdateDTO = new UpdateBookRecordDTO("Updated Book Title", "Updated Author", LocalDate.now(), 10, 1);
        int validBookId = 1;

        assertDoesNotThrow(() -> bookValidation.update(validUpdateDTO, validBookId));
    }

    // Testando atualização sem alteração
    @Test
    void shouldNotChangeBookWhenDataIsSame() {
        UpdateBookRecordDTO sameDataDTO = new UpdateBookRecordDTO("Valid Book Title", "Author Name", LocalDate.now(), 5, 1);
        int validBookId = 1;

        assertDoesNotThrow(() -> bookValidation.update(sameDataDTO, validBookId));
    }
}
