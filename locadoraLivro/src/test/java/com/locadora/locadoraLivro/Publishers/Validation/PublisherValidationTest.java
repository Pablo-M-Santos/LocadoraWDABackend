package com.locadora.locadoraLivro.Publishers.Validation;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import com.locadora.locadoraLivro.Publishers.services.PublisherServices;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherValidationTest {

    @Mock
    private PublisherServices publisherServices;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RentRepository rentRepository;

    @InjectMocks
    private PublisherValidation publisherValidation;

    private CreatePublisherRequestDTO createPublisherRequestDTO;
    private UpdatePublisherRecordDTO updatePublisherRecordDTO;
    private PublisherModel existingPublisher;

    @BeforeEach
    void setUp() {
        createPublisherRequestDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", "123456789", "www.publisher.com");
        updatePublisherRecordDTO = new UpdatePublisherRecordDTO("Updated Publisher Name", "updated@example.com", "123456789", "www.updated.com");
        existingPublisher = new PublisherModel("Existing Publisher", "existing@example.com", "123456789", "www.existing.com");
    }

    // Nome já em uso
    @Test
    void shouldThrowExceptionWhenPublisherNameAlreadyExists() {
        when(publisherRepository.findByNameAndIsDeletedFalse(createPublisherRequestDTO.name())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> {
            publisherValidation.validateName(createPublisherRequestDTO);
        }, "Nome da editora já em uso.");
    }

    // Nome nulo ou vazio
    @Test
    void shouldThrowExceptionWhenNameIsNullOrEmpty() {
        CreatePublisherRequestDTO nullNameDTO = new CreatePublisherRequestDTO(null, "publisher@example.com", "123456789", "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullNameDTO), "O nome não pode estar vazio ou conter apenas espaços.");

        CreatePublisherRequestDTO emptyNameDTO = new CreatePublisherRequestDTO("", "publisher@example.com", "123456789", "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyNameDTO), "O nome não pode estar vazio ou conter apenas espaços.");
    }

    // Email em uso
    @Test
    void shouldThrowExceptionWhenEmailIsInUse() {
        when(publisherRepository.findByEmail(createPublisherRequestDTO.email())).thenReturn(existingPublisher);
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    // Email nulo ou vazio
    @Test
    void shouldThrowExceptionWhenEmailIsNullOrEmpty() {
        CreatePublisherRequestDTO nullEmailDTO = new CreatePublisherRequestDTO("Publisher Name", null, "123456789", "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullEmailDTO), "O e-mail não pode ficar vazio.");

        CreatePublisherRequestDTO emptyEmailDTO = new CreatePublisherRequestDTO("Publisher Name", "", "123456789", "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyEmailDTO), "O e-mail não pode ficar vazio.");
    }

    // Telefone em uso
    @Test
    void shouldThrowExceptionWhenTelephoneIsInUse() {
        when(publisherRepository.findByTelephone(createPublisherRequestDTO.telephone())).thenReturn(existingPublisher);
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    // Telefone nulo ou vazio
    @Test
    void shouldThrowExceptionWhenTelephoneIsNullOrEmpty() {
        CreatePublisherRequestDTO nullTelephoneDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", null, "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(nullTelephoneDTO), "Telefone não pode ser vazio.");

        CreatePublisherRequestDTO emptyTelephoneDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", "", "www.publisher.com");
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(emptyTelephoneDTO), "Telefone não pode ser vazio.");
    }

    // Site em uso
    @Test
    void shouldThrowExceptionWhenSiteIsInUse() {
        when(publisherRepository.findBySite(createPublisherRequestDTO.site())).thenReturn(existingPublisher);
        assertThrows(CustomValidationException.class, () -> publisherValidation.create(createPublisherRequestDTO));
    }

    // Site nulo ou vazio
    @Test
    void shouldNotThrowExceptionWhenSiteIsNullOrEmpty() {
        CreatePublisherRequestDTO nullSiteDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", "123456789", null);
        assertDoesNotThrow(() -> publisherValidation.create(nullSiteDTO));

        CreatePublisherRequestDTO emptySiteDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", "123456789", "");
        assertDoesNotThrow(() -> publisherValidation.create(emptySiteDTO));
    }

    // Criação com dados válidos
    @Test
    void shouldCreateWhenValidData() {
        // Arrange: inicialize o DTO e configure os stubs
        when(publisherRepository.findByNameAndIsDeletedFalse(createPublisherRequestDTO.name())).thenReturn(null);
        when(publisherRepository.findByEmail(createPublisherRequestDTO.email())).thenReturn(null);
        when(publisherRepository.findByTelephone(createPublisherRequestDTO.telephone())).thenReturn(null);
        when(publisherRepository.findBySite(createPublisherRequestDTO.site())).thenReturn(null);

        publisherValidation.create(createPublisherRequestDTO);

        verify(publisherRepository).findByNameAndIsDeletedFalse(createPublisherRequestDTO.name());
        verify(publisherRepository).findByEmail(createPublisherRequestDTO.email());
        verify(publisherRepository).findByTelephone(createPublisherRequestDTO.telephone());
        verify(publisherRepository).findBySite(createPublisherRequestDTO.site());
    }


    // Atualizar com nome existente
    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingName() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findByName(updatePublisherRecordDTO.name())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    // Atualizar com email existente
    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingEmail() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findByEmail(updatePublisherRecordDTO.email())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    // Atualizar com telefone existente
    @Test
    void shouldThrowExceptionWhenTelephoneAlreadyExists() {
        when(publisherRepository.findByTelephone(createPublisherRequestDTO.telephone())).thenReturn(existingPublisher);

        assertThrows(CustomValidationException.class, () -> {
            publisherValidation.validateTelephone(createPublisherRequestDTO);
        }, "Este telefone já está em uso.");
    }


    // Atualizar com site existente
    @Test
    void shouldThrowExceptionWhenUpdatingWithExistingSite() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));
        when(publisherRepository.findBySite(updatePublisherRecordDTO.site())).thenReturn(new PublisherModel());

        assertThrows(CustomValidationException.class, () -> publisherValidation.update(updatePublisherRecordDTO, existingPublisher.getId()));
    }

    // Teste para exclusão de uma editora existente
    @Test
    void shouldDeleteExistingPublisher() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.of(existingPublisher));

        assertDoesNotThrow(() -> publisherValidation.delete(existingPublisher.getId()));

        verify(publisherRepository).deleteById(existingPublisher.getId());
    }


    // Teste para tentar excluir uma editora que não existe
    @Test
    void shouldThrowExceptionWhenDeletingNonExistingPublisher() {
        when(publisherRepository.findById(existingPublisher.getId())).thenReturn(Optional.empty());

        assertThrows(CustomValidationException.class, () -> publisherValidation.delete(existingPublisher.getId()), "Editora não encontrada.");
    }

    // Teste para deletar editora sem livro associado
    @Test
    public void testDeleteSuccess() {
        when(publisherRepository.findById(1)).thenReturn(Optional.of(existingPublisher));
        when(bookRepository.findByPublisherId(1)).thenReturn(List.of());

        publisherValidation.delete(1);

        verify(publisherRepository, times(1)).deleteById(1);
    }

    // Teste para deletar editora não existente
    @Test
    public void testDeletePublisherNotFound() {
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        CustomValidationException thrown = assertThrows(CustomValidationException.class, () -> {
            publisherValidation.delete(1);
        });
        assertEquals("Editora não encontrada.", thrown.getMessage());
    }

    // Teste para deletar editora com livros associados
    @Test
    public void testDeleteBooksAreRented() {
        when(publisherRepository.findById(1)).thenReturn(Optional.of(existingPublisher));
        PublisherModel publisher = new PublisherModel("Publisher Name", "publisher@example.com", "123456789", "www.publisher.com");
        BookModel book = new BookModel("Book Name", "Author Name", LocalDate.now(), 10, publisher);
        when(bookRepository.findByPublisherId(1)).thenReturn(List.of(book));
        when(rentRepository.existsByBookIdAndStatus(book.getId(), RentStatusEnum.ALUGADO)).thenReturn(true);

        CustomValidationException thrown = assertThrows(CustomValidationException.class, () -> {
            publisherValidation.delete(1);
        });

        assertEquals("Não é possível excluir o editor. Existem livros atualmente alugados.", thrown.getMessage());
    }


}
