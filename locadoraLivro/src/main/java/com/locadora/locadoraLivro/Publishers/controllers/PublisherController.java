package com.locadora.locadoraLivro.Publishers.controllers;

import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.PublisherResponseDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRecordDTO;
import com.locadora.locadoraLivro.Publishers.mappers.PublisherMapper;
import com.locadora.locadoraLivro.Publishers.services.PublisherServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


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
    public ResponseEntity<Object> getAll(String search, @RequestParam(required = false) Integer page){
        if (page == null) {
            return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponseList(publisherServices.findAllWithoutPagination(search)));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(publisherServices.findAll(search, page).map(publisherMapper::toPublisherResponse));
        }
    }

    @GetMapping("/publisher/{id}")
    public ResponseEntity<PublisherResponseDTO> getById(@PathVariable(value = "id") int id){
        return ResponseEntity.status(HttpStatus.OK).body(publisherMapper.toPublisherResponse(publisherServices.findById(id).get()));
    }

    @PutMapping("/publisher/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdatePublisherRecordDTO updatePublisherRecordDTO){
        return publisherServices.update(id, updatePublisherRecordDTO);
    }

    @DeleteMapping("/publisher/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id){
        return publisherServices.delete(id);
    }
}