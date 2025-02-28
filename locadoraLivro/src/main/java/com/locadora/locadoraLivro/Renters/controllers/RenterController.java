package com.locadora.locadoraLivro.Renters.controllers;

import com.locadora.locadoraLivro.Renters.DTOs.CreateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.DTOs.RenterResponseDTO;
import com.locadora.locadoraLivro.Renters.DTOs.UpdateRenterRequestDTO;
import com.locadora.locadoraLivro.Renters.mappers.RenterMapper;
import com.locadora.locadoraLivro.Renters.services.RenterServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
public class RenterController {
    @Autowired
    RenterMapper renterMapper;

    @Autowired
    RenterServices renterServices;

    @PostMapping("/renter")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateRenterRequestDTO data) {
        return renterServices.create(data);
    }

    @GetMapping("/renter")
    public ResponseEntity<Object> getAll(String search, @RequestParam(required = false) Integer page) {
        if (page == null) {
            return ResponseEntity.status(HttpStatus.OK).body(renterMapper.toRenterResponseList(renterServices.findAllWithoutPagination(search)));
        } else {
            return ResponseEntity.status(HttpStatus.OK).body(renterServices.findAll(search, page).map(renterMapper::toRenterResponse));
        }
    }

    @GetMapping("/renter/{id}")
    public ResponseEntity<RenterResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(renterMapper.toRenterResponse(renterServices.findById(id).get()));
    }

    @PutMapping("/renter/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdateRenterRequestDTO updateRenterRequestDTO) {
        return renterServices.update(id, updateRenterRequestDTO);
    }

    @DeleteMapping("/renter/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id) {
        return renterServices.delete(id);
    }
}
