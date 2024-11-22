package com.locadora.locadoraLivro.Users.controllers;

import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UserResponseDTO;
import com.locadora.locadoraLivro.Users.mappers.UserMapper;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import com.locadora.locadoraLivro.Users.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:8080")
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserServices userServices;

    @Autowired
    UserRepository userRepository;

    @PostMapping("/user")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateUserRequestDTO data) {
        return userServices.create(data);
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserResponseDTO>> getAll(String search){
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponseList(userServices.findAll(search)));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponse(userServices.findById(id).get()));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> update(@PathVariable(value="id") int id, @RequestBody @Valid UpdateUserRequestDTO updateUserRequestDTO){
        return userServices.update(id, updateUserRequestDTO);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value="id") int id){
        return userServices.delete(id);
    }

}
