package com.locadora.locadoraLivro.Rents.services;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Rents.DTOs.CreateRentRequestDTO;
import com.locadora.locadoraLivro.Rents.DTOs.UpdateRentRecordDTO;
import com.locadora.locadoraLivro.Rents.Validation.RentValidation;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentServicesTest {

    @InjectMocks
    private RentServices rentServices;

    @Mock
    private RentRepository rentRepository;

    @Mock
    private RenterRepository renterRepository;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private RentValidation rentValidation;

    private RenterModel renter;
    private BookModel book;
    private RentModel rent;

    @BeforeEach
    void setUp() {
        renter = new RenterModel("John Doe", "johndoe@example.com", "123456789", "123 Main St", "12345678901");
        book = new BookModel("Book Title", "Author", LocalDate.now(), 5, null);
        rent = new RentModel(renter, book, LocalDate.now().plusDays(7), LocalDate.now());
    }

    @Test
    void testCreateRent() {
        CreateRentRequestDTO createRentRequestDTO = new CreateRentRequestDTO(renter.getId(), book.getId(), LocalDate.now().plusDays(7));

        when(renterRepository.findById(renter.getId())).thenReturn(Optional.of(renter));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<Void> response = rentServices.create(createRentRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(rentRepository).save(any(RentModel.class));
        verify(bookRepository).save(book);
    }

    @Test
    void testFindAllWithPagination() {
        Page<RentModel> rentPage = new PageImpl<>(List.of(rent));
        when(rentRepository.findAll(any(PageRequest.class))).thenReturn(rentPage);

        Page<RentModel> result = rentServices.findAll("", 0);

        assertFalse(result.isEmpty());
        assertEquals(1, result.getTotalElements());
        verify(rentRepository).findAll(any(PageRequest.class));
    }

    @Test
    void testDelivered() {
        int rentId = 1;
        when(rentRepository.findById(rentId)).thenReturn(Optional.of(rent));

        ResponseEntity<Object> response = rentServices.delivered(rentId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(rent.getDevolutionDate());
        verify(rentRepository).save(rent);
    }

    @Test
    void testUpdateRent() {
        int rentId = 1;
        UpdateRentRecordDTO updateDTO = new UpdateRentRecordDTO(renter.getId(), book.getId(), LocalDate.now().plusDays(7), null);

        when(rentRepository.findById(rentId)).thenReturn(Optional.of(rent));
        when(renterRepository.findById(renter.getId())).thenReturn(Optional.of(renter));
        when(bookRepository.findById(book.getId())).thenReturn(Optional.of(book));

        ResponseEntity<Object> response = rentServices.update(rentId, updateDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(rentRepository).save(rent);
    }

    @Test
    void testFindByIdRentNotFound() {
        int rentId = 999;
        when(rentRepository.findById(rentId)).thenReturn(Optional.empty());

        assertThrows(ModelNotFoundException.class, () -> rentServices.findById(rentId));

        verify(rentRepository, times(1)).findById(rentId);
    }
}
