package com.locadora.locadoraLivro.Renters.services;

import com.locadora.locadoraLivro.Renters.DTOs.CreateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.Validation.RenterValidation;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenterServicesTest {

    @Mock
    private RenterRepository renterRepository;

    @Mock
    private RenterValidation renterValidation;

    @InjectMocks
    private RenterServices renterServices;

    private RenterModel renter;

    @BeforeEach
    void setUp() {
        renter = new RenterModel("John Doe", "john@example.com", "123456789", "123 Street", "12345678901");
    }

    // Testa a criação do locatário
    @Test
    void testCreateRenter() {
        CreateRenterRequestDTO requestDTO = new CreateRenterRequestDTO("John Doe", "john@example.com", "123456789", "123 Street", "12345678901");
        doNothing().when(renterValidation).create(requestDTO);
        when(renterRepository.save(any(RenterModel.class))).thenReturn(renter);

        ResponseEntity<Void> response = renterServices.create(requestDTO);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        verify(renterValidation).create(requestDTO);
        verify(renterRepository).save(any(RenterModel.class));
    }

    // Testa a busca de locatários sem paginação
    @Test
    void testFindAllWithoutPagination() {
        when(renterRepository.findAllByIsDeletedFalse((Sort) any())).thenReturn(List.of(renter));

        List<RenterModel> renters = renterServices.findAllWithoutPagination("");
        assertFalse(renters.isEmpty());
        assertEquals(1, renters.size());
    }

    // Testa a atualização do locatário existente
    @Test
    void testUpdateRenter() {
        UpdateRenterRequestDTO updateDTO = new UpdateRenterRequestDTO("Jane Doe", "jane@example.com", "987654321", "456 Street", "12345678901");
        when(renterRepository.findById(1)).thenReturn(Optional.of(renter));
        doNothing().when(renterValidation).update(updateDTO, 1);
        when(renterRepository.save(any(RenterModel.class))).thenReturn(renter);

        ResponseEntity<Object> response = renterServices.update(1, updateDTO);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(renterRepository).save(any(RenterModel.class));
    }

    // Testa a atualização de um locatário inexistente
    @Test
    void testUpdateNonExistentRenter() {
        UpdateRenterRequestDTO updateDTO = new UpdateRenterRequestDTO("Jane Doe", "jane@example.com", "987654321", "456 Street", "12345678901");
        when(renterRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = renterServices.update(1, updateDTO);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Renter not found", response.getBody());
    }

    // Testa a deleção de um locatário existente
    @Test
    void testDeleteRenter() {
        when(renterRepository.findById(1)).thenReturn(Optional.of(renter));
        doNothing().when(renterValidation).validateDeleteRenter(1);

        ResponseEntity<Object> response = renterServices.delete(1);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Renter deleted successfully", response.getBody());
    }

    // Testa a deleção de um locatário inexistente
    @Test
    void testDeleteNonExistentRenter() {
        when(renterRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = renterServices.delete(1);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Renter not found", response.getBody());
    }
}
