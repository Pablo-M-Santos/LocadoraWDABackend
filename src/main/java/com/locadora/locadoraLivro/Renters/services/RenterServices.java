package com.locadora.locadoraLivro.Renters.services;

import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Renters.DTOs.CreateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.Validation.CpfValidation;
import com.locadora.locadoraLivro.Renters.Validation.RenterEmailValidation;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
public class RenterServices {

    @Autowired
    private RenterRepository renterRepository;

    @Autowired
    private RenterEmailValidation renterEmailValidation;

    @Autowired
    private CpfValidation cpfValidation;

    public ResponseEntity<Void> create(@RequestBody @Valid CreateRenterRequestDTO data) {
        renterEmailValidation.validateEmail(data.email());
        cpfValidation.validateCpf(data.cpf());

        RenterModel newRenter = new RenterModel(data.name(), data.email(), data.telephone(), data.address(), data.cpf());
        renterRepository.save(newRenter);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<RenterModel> findAll(String search) {
        List<RenterModel> renters = renterRepository.findAllByIsDeletedFalse(Sort.by(Sort.Direction.DESC, "id"));
        if (renters.isEmpty()) throw new ModelNotFoundException();
        return renters;
    }

    public Optional<RenterModel> findById(int id) {
        return renterRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateRenterRequestDTO updateRenterRequestDTO) {
        Optional<RenterModel> response = renterRepository.findById(id);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Renter not found");
        }
        var renterModel = response.get();
        BeanUtils.copyProperties(updateRenterRequestDTO, renterModel);
        return ResponseEntity.status(HttpStatus.OK).body(renterRepository.save(renterModel));
    }


    public ResponseEntity<Object> delete(int id){
        Optional<RenterModel> response = renterRepository.findById(id);
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Renter not found");
        }

        renterRepository.delete(response.get());

        RenterModel renter = response.get();

        renter.setDeleted(true);

        renterRepository.save(renter);

        return ResponseEntity.status(HttpStatus.OK).body("Renter deleted successfully");
    }
}