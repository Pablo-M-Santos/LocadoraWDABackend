package com.locadora.locadoraLivro.Publishers.controllers;

import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.PublisherResponseDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.mappers.PublisherMapper;
import com.locadora.locadoraLivro.Publishers.services.PublisherServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PublisherController {

    @Autowired
    PublisherMapper publisherMapper;

    @Autowired
    PublisherServices publisherServices;

    @PostMapping("/publisher")
    public ResponseEntity<Void> create(@RequestBody @Valid CreatePublisherRequestDTO data) {
        return publisherServices.create(data);
    }

    @GetMapping("/publisher")
    public ResponseEntity<List<PublisherResponseDTO>> getAll(String search) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponseList(publisherServices.findAll(search)));
    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponse(publisherServices.findById(id).orElseThrow(() -> new RuntimeException("Renter not found"))));
    }

    @PutMapping("/publisher/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdatePublisherRequestDTO updatePublisherRequestDTO) {
        return publisherServices.update(id, updatePublisherRequestDTO);
    }

    @DeleteMapping("/publisher/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id) {
        return publisherServices.delete(id);
    }
}