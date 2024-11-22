package com.locadora.locadoraLivro.Books.services;

import com.locadora.locadoraLivro.Books.DTOs.CreateBookRequestDTO;
import com.locadora.locadoraLivro.Books.DTOs.UpdateBookRecordDTO;
import com.locadora.locadoraLivro.Books.Validation.BookValidation;
import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import com.locadora.locadoraLivro.Publishers.repositories.PublisherRepository;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class BookServices {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    PublisherRepository publisherRepository;

    @Autowired
    RentRepository rentRepository;

    @Autowired
    BookValidation bookValidation;

    public ResponseEntity<Void> create(@Valid CreateBookRequestDTO data) {

        bookValidation.create(data);

        PublisherModel publisher = publisherRepository.findById(data.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada."));

        BookModel newBook = new BookModel(data.name(), data.author(), data.launchDate(), data.totalQuantity(), publisher);
        bookRepository.save(newBook);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public Page<BookModel> findAll(String search, int page){
        int size = 5;
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        if (Objects.equals(search, "")){
            Page<BookModel> books = bookRepository.findAllByIsDeletedFalse(pageable);
            if(books.isEmpty()) throw new ModelNotFoundException();

            for (BookModel book : books) {
                List<RentModel> totalRented = rentRepository.findAllByBookIdAndStatus(book.getId(), RentStatusEnum.RENTED);
                List<RentModel> totalLate = rentRepository.findAllByBookIdAndStatus(book.getId(), RentStatusEnum.LATE);
                book.setTotalInUse(totalRented.size() + totalLate.size());
            }

            return books;

        } else {
            Page<BookModel> bookSearch = bookRepository.findAllByName(search, pageable);
            return bookSearch;
        }
    }

    public Optional<BookModel> findById(int id){
        return bookRepository.findById(id);
    }

    public ResponseEntity<Object> update(int id, @Valid UpdateBookRecordDTO updateBookRecordDTO){
        Optional<BookModel> response = bookRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");

        bookValidation.update(updateBookRecordDTO);

        PublisherModel publisher = publisherRepository.findById(updateBookRecordDTO.publisherId())
                .orElseThrow(() -> new IllegalArgumentException("Editora não encontrada."));

        var bookModel = response.get();
        bookModel.setName(updateBookRecordDTO.name());
        bookModel.setAuthor(updateBookRecordDTO.author());
        bookModel.setLaunchDate(updateBookRecordDTO.launchDate());
        bookModel.setTotalQuantity(updateBookRecordDTO.totalQuantity());
        bookModel.setPublisher(publisher);

        bookRepository.save(bookModel);
        return ResponseEntity.status(HttpStatus.OK).body(bookModel);
    }

    public ResponseEntity<Object> delete(int id){
        Optional<BookModel> response = bookRepository.findById(id);
        if (response.isEmpty()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livro não encontrado.");

        bookValidation.validDeleteBook(id);

        BookModel book = response.get();

        book.setDeleted(true);

        bookRepository.save(book);

        return ResponseEntity.status(HttpStatus.OK).body("Livro excluído com sucesso.");
    }
}