package com.locadora.locadoraLivro.dashboard.mappers;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import com.locadora.locadoraLivro.dashboard.DTOs.BooksMoreRented;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BookRentMapper {

    @Autowired
    private RentRepository rentRepository;

    public List<BooksMoreRented> toBooksMoreRentedList(List<BookModel> bookList, int numberOfMonths) {
        return bookList.stream()
                .map(book -> toBooksMoreRented(book, numberOfMonths))
                .sorted((b1, b2) -> Integer.compare(b2.totalRents(), b1.totalRents()))
                .limit(3)
                .collect(Collectors.toList());
    }

    private BooksMoreRented toBooksMoreRented(BookModel book, int numberOfMonths) {
        List<RentModel> rentsInLastYear = rentRepository.findAllByBookId(book.getId()).stream()
                .filter(rent -> !rent.getRentDate().isBefore(LocalDate.now().minusMonths(numberOfMonths))
                        && !rent.getRentDate().isAfter(LocalDate.now()))
                .collect(Collectors.toList());

        int rentCount = rentsInLastYear.size();
        return new BooksMoreRented(book.getName(), rentCount);
    }
}
