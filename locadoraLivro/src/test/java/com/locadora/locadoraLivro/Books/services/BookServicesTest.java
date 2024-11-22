package com.locadora.locadoraLivro.Books.services;

import com.locadora.locadoraLivro.Books.DTOs.CreateBookRequestDTO;
import com.locadora.locadoraLivro.Books.DTOs.UpdateBookRecordDTO;
import com.locadora.locadoraLivro.Books.Validation.BookValidation;
import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServicesTest {

    @InjectMocks
    private BookServices bookServices;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private BookValidation bookValidation;


    // Testa a criação do Livro
    @Test
    void testCreateBook() {
        CreateBookRequestDTO createDTO = new CreateBookRequestDTO("Valid Book Title", "Author Name",LocalDate.now(), 5, 1);
        PublisherModel publisher = new PublisherModel();
        publisher.setId(1);

        when(publisherRepository.findById(createDTO.publisherId())).thenReturn(Optional.of(publisher));

        ResponseEntity<Void> response = bookServices.create(createDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(bookRepository, times(1)).save(any(BookModel.class));
    }

    // Testa a busca de todos os livros sem filtro de busca
    @Test
    void testFindAllBooks_NoSearch() {
        PageRequest pageable = PageRequest.of(0, 8, Sort.by("id").descending());
        List<BookModel> books = List.of(new BookModel(), new BookModel());
        Page<BookModel> bookPage = new PageImpl<>(books);

        when(bookRepository.findAllByIsDeletedFalse(pageable)).thenReturn(bookPage);

        Page<BookModel> result = bookServices.findAll("", 0);

        assertEquals(2, result.getContent().size());
        verify(bookRepository, times(1)).findAllByIsDeletedFalse(pageable);
    }

    // Testa a busca de livros com filtro de busca
    @Test
    void testFindAllBooks_WithSearch() {
        String search = "Book";
        PageRequest pageable = PageRequest.of(0, 8, Sort.by("id").descending());
        List<BookModel> books = List.of(new BookModel(), new BookModel());
        Page<BookModel> bookPage = new PageImpl<>(books);

        when(bookRepository.findAllByName(search, pageable)).thenReturn(bookPage);

        Page<BookModel> result = bookServices.findAll(search, 0);

        assertEquals(2, result.getContent().size());
        verify(bookRepository, times(1)).findAllByName(search, pageable);
    }

    // Testa a busca de um livro pelo ID quando o livro existe
    @Test
    void testFindById_BookExists() {
        BookModel book = new BookModel();
        when(bookRepository.findById(1)).thenReturn(Optional.of(book));

        Optional<BookModel> result = bookServices.findById(1);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
    }

    // Testa a busca de um livro pelo ID quando o livro não existe
    @Test
    void testFindById_BookNotFound() {
        when(bookRepository.findById(1)).thenReturn(Optional.empty());

        Optional<BookModel> result = bookServices.findById(1);

        assertTrue(result.isEmpty());
    }

    // Testa a atualização de um livro existente
    @Test
    void testUpdateBook() {
        int bookId = 1;
        UpdateBookRecordDTO updateDTO = new UpdateBookRecordDTO("Updated Title", "Updated Author", LocalDate.now(), 10, 1);
        BookModel book = new BookModel();
        PublisherModel publisher = new PublisherModel();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(publisherRepository.findById(updateDTO.publisherId())).thenReturn(Optional.of(publisher));

        ResponseEntity<Object> response = bookServices.update(bookId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookRepository, times(1)).save(book);
        assertEquals("Updated Title", book.getName());
        assertEquals(10, book.getTotalQuantity());
    }

    // Testa a atualização de um livro que não existe
    void testUpdateBook_BookNotFound() {
        int bookId = 1;
        UpdateBookRecordDTO updateDTO = new UpdateBookRecordDTO("Updated Title", "Updated Author", LocalDate.now(), 10, 1);

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = bookServices.update(bookId, updateDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
    }

    // Testa a exclusão de um livro existente
    @Test
    void testDeleteBook_BookExists() {
        int bookId = 1;
        BookModel book = new BookModel();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));

        ResponseEntity<Object> response = bookServices.delete(bookId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Book deleted successfully", response.getBody());
        assertTrue(book.isDeleted());
    }

    // Testa a exclusão de um livro que não existe
    @Test
    void testDeleteBook_BookNotFound() {
        int bookId = 1;

        when(bookRepository.findById(bookId)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = bookServices.delete(bookId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Book not found", response.getBody());
    }
}
