package com.locadora.locadoraLivro.Publishers.services;

import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Publishers.DTOs.CreatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.UpdatePublisherRequestDTO;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PublisherServices {


    @Autowired
    private PublisherRepository publisherRepository;

    public ResponseEntity<Void> create(@Valid CreatePublisherRequestDTO data) {
        if (publisherRepository.findByName(data.name()) != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        PublisherModel newPublisher = new PublisherModel(data.name(), data.email(), data.telephone(), data.site());
        publisherRepository.save(newPublisher);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<PublisherModel> findAll(String search) {
        List<PublisherModel> publisher = publisherRepository.findAllByIsDeletedFalse();
        if (publisher.isEmpty()) throw new ModelNotFoundException();
        return publisher;
    }

    public Optional<PublisherModel> findById(int id) {
        return publisherRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdatePublisherRequestDTO updatePublisherRequestDTO){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if(response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        var publisherModel = response.get();
        BeanUtils.copyProperties(updatePublisherRequestDTO, publisherModel);
        return ResponseEntity.status(HttpStatus.OK).body(publisherRepository.save(publisherModel));
    }

    public ResponseEntity<Object> delete(int id){
        Optional<PublisherModel> response = publisherRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Publisher not found");

        PublisherModel publisher = response.get();

        publisher.setDeleted(true);

        publisherRepository.save(publisher);

        return ResponseEntity.status(HttpStatus.OK).body("Publisher deleted successfully");
    }
}