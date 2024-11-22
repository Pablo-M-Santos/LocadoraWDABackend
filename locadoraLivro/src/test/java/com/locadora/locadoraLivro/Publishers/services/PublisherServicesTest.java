package com.locadora.locadoraLivro.Publishers.services;

import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.locadoraLivro.Publishers.Validation.PublisherValidation;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PublisherServicesTest {

    @Mock
    private PublisherRepository publisherRepository;

    @Mock
    private PublisherValidation publisherValidation;

    @InjectMocks
    private PublisherServices publisherServices;

    private CreatePublisherRequestDTO createPublisherRequestDTO;
    private UpdatePublisherRecordDTO updatePublisherRecordDTO;
    private PublisherModel publisherModel;

    @BeforeEach
    void setUp() {
        createPublisherRequestDTO = new CreatePublisherRequestDTO("Publisher Name", "publisher@example.com", "1234567890", "http://publisher.com");
        updatePublisherRecordDTO = new UpdatePublisherRecordDTO("Updated Publisher Name", "updated@example.com", "0987654321", "http://updatedpublisher.com");
        publisherModel = new PublisherModel("Publisher Name", "publisher@example.com", "1234567890", "http://publisher.com");
    }

    // Testa a criação de editora
    @Test
    void shouldCreatePublisher() {
        doNothing().when(publisherValidation).validateName(createPublisherRequestDTO);
        doNothing().when(publisherValidation).validateEmail(createPublisherRequestDTO);
        doNothing().when(publisherValidation).validateTelephone(createPublisherRequestDTO);
        doNothing().when(publisherValidation).validateSite(createPublisherRequestDTO);

        ResponseEntity<Void> response = publisherServices.create(createPublisherRequestDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());

        ArgumentCaptor<PublisherModel> publisherCaptor = ArgumentCaptor.forClass(PublisherModel.class);
        verify(publisherRepository, times(1)).save(publisherCaptor.capture());
        PublisherModel savedPublisher = publisherCaptor.getValue();
        assertEquals("Publisher Name", savedPublisher.getName());
    }

    // Testa a busca de todos os editores sem paginação
    @Test
    void shouldFindAllPublishersWithoutPagination() {
        when(publisherRepository.findAllByIsDeletedFalse((Pageable) any())).thenReturn((Page<PublisherModel>) Collections.singletonList(publisherModel));

        var result = publisherServices.findAllWithoutPagination("");

        assertEquals(1, result.size());
        assertEquals(publisherModel, result.get(0));
    }

    // Testa a atualização de um editor existente
    @Test
    void shouldUpdatePublisher() {
        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisherModel));
        doNothing().when(publisherValidation).validateNameUpdate(updatePublisherRecordDTO, 1);
        doNothing().when(publisherValidation).validateEmailUpdate(updatePublisherRecordDTO, 1);
        doNothing().when(publisherValidation).validateTelephoneUpdate(updatePublisherRecordDTO, 1);
        doNothing().when(publisherValidation).validateSiteUpdate(updatePublisherRecordDTO, 1);

        ResponseEntity<Object> response = publisherServices.update(1, updatePublisherRecordDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(publisherModel.getName(), updatePublisherRecordDTO.name());
        verify(publisherRepository, times(1)).save(publisherModel);
    }

    // Testa a atualização de um editor que não existe
    @Test
    void shouldReturnNotFoundWhenUpdatingNonExistentPublisher() {
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = publisherServices.update(1, updatePublisherRecordDTO);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Publisher not found", response.getBody());
    }

    // Testa a exclusão de um editor
    @Test
    void shouldDeletePublisher() {
        when(publisherRepository.findById(1)).thenReturn(Optional.of(publisherModel));

        ResponseEntity<Object> response = publisherServices.delete(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Publisher deleted successfully", response.getBody());
        verify(publisherRepository, times(1)).save(publisherModel);
    }

    // Testa a exclusão de um editor que não existe
    @Test
    void shouldReturnNotFoundWhenDeletingNonExistentPublisher() {
        when(publisherRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<Object> response = publisherServices.delete(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Publisher not found", response.getBody());
    }
}
