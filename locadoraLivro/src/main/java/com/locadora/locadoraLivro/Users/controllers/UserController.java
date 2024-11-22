package com.locadora.locadoraLivro.Users.controllers;

import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UserResponseDTO;
import com.locadora.locadoraLivro.Users.mappers.UserMapper;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.models.UserRoleEnum;
import com.locadora.locadoraLivro.Users.services.UserServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserServices userServices;

    @PostMapping("/user")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateUserRequestDTO data) {
        return userServices.create(data);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getAll(String search, @RequestParam(required = false) Integer page, @RequestParam(required = false) String role){
        if (page == null) {
            return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponseList(userServices.findAllWithoutPagination(search)));
        }

        if (!role.isEmpty() && role != null){
            return ResponseEntity.status(HttpStatus.OK).body(userServices.findAllByRole(search, page, role).map(userMapper::toUserResponse));
        }

        return ResponseEntity.status(HttpStatus.OK).body(userServices.findAll(search, page).map(userMapper::toUserResponse));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(userMapper.toUserResponse(userServices.findById(id).get()));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Object> update(@PathVariable(value="id") int id, @RequestBody @Valid UpdateUserRequestDTO updateUserRequestDTO){
        return userServices.update(id, updateUserRequestDTO);
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value="id") int id){
        return userServices.delete(id);
    }
}