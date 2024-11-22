package com.locadora.locadoraLivro.Books.controllers;

import com.locadora.locadoraLivro.Books.DTOs.CreateBookRequestDTO;
import com.locadora.locadoraLivro.Books.DTOs.BookResponseDTO;
import com.locadora.locadoraLivro.Books.DTOs.UpdateBookRecordDTO;
import com.locadora.locadoraLivro.Books.mappers.BookMapper;
import com.locadora.locadoraLivro.Books.services.BookServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class BookController {

    @Autowired
    BookMapper bookMapper;

    @Autowired
    BookServices bookServices;

    @PostMapping("/book")
    public ResponseEntity<Void> create(@RequestBody @Valid CreateBookRequestDTO data) {
        return bookServices.create(data);
    }

    @GetMapping("/book")
    public ResponseEntity<List<BookResponseDTO>> getAll() {
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookResponseList(bookServices.findAll()));
    }

    @GetMapping("/book/{id}")
    public ResponseEntity<BookResponseDTO> getById(@PathVariable(value = "id") int id) {
        return ResponseEntity.status(HttpStatus.OK).body(bookMapper.toBookResponse(bookServices.findById(id).get()));
    }

    @PutMapping("/book/{id}")
    public ResponseEntity<Object> update(@PathVariable(value = "id") int id, @RequestBody @Valid UpdateBookRecordDTO updateBookRecordDTO) {
        return bookServices.update(id, updateBookRecordDTO);
    }

    @DeleteMapping("/book/{id}")
    public ResponseEntity<Object> delete(@PathVariable(value = "id") int id) {
        return bookServices.delete(id);
    }
}