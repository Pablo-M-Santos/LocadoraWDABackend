package com.locadora.locadoraLivro.Rents.Validation;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Exceptions.CustomValidationException;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Rents.DTOs.CreateRentRequestDTO;
import com.locadora.locadoraLivro.Rents.DTOs.UpdateRentRecordDTO;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@AllArgsConstructor
@Component
public class RentValidation {

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RentRepository rentRepository;

    public void validateRenterId(CreateRentRequestDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Locatário não encontrado");
        }

        RenterModel renter = renterRepository.findById(data.renterId()).get();

        if (renter.isDeleted()){
            throw new CustomValidationException("Locatário não encontrado");
        }
    }

    public void validateRenterIdUpdate(UpdateRentRecordDTO data){
        if (renterRepository.findById(data.renterId()).isEmpty()){
            throw new CustomValidationException("Locatário não encontrado");
        }
    }

    public void validateBookId(CreateRentRequestDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Livro não encontrado");
        }

        BookModel book = bookRepository.findById(data.bookId()).get();

        if (book.isDeleted()){
            throw new CustomValidationException("Livro não encontrado");
        }
    }

    public void validateBookIdUpdate(UpdateRentRecordDTO data){
        if (bookRepository.findById(data.bookId()).isEmpty()){
            throw new CustomValidationException("Livro não encontrado");
        }
    }

    public void validateDeadLine(CreateRentRequestDTO data){
        if (data.deadLine().isAfter(LocalDate.now().plusDays(30))){
            throw new CustomValidationException("Prazo de entrega não pode ser mais de 30 dias.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("O prazo de entrega não pode ser no passado.");
        }
    }

    public void validateDeadLineUpdate(UpdateRentRecordDTO data) {
        if (data.deadLine().isAfter(LocalDate.now().plusDays(30))) {
            throw new CustomValidationException("Prazo de entrega não pode ser mais de 30 dias.");
        } else if (data.deadLine().isBefore(LocalDate.now())) {
            throw new CustomValidationException("O prazo de entrega não pode ser no passado.");
        }
    }

    public void validateBookTotalQuantity(BookModel data){
        if (data.getTotalQuantity() <= 0){
            throw new CustomValidationException("Não há livros disponíveis");
        }
    }

    public void validateRentRepeated(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndBookIdAndStatus(data.renterId(), data.bookId(), RentStatusEnum.ALUGADO)){
            throw new CustomValidationException("Locatário já tem este livro alugado.");
        }
    }

    public void validateRentLate(CreateRentRequestDTO data){
        if (rentRepository.existsByRenterIdAndStatus(data.renterId(), RentStatusEnum.ATRASADO)){
            throw new CustomValidationException("Locatário tem aluguel atrasado.");
        }
    }

    public void setRentStatus(RentModel rent){
        if (rent.getDevolutionDate() == null){

            if (rent.getDeadLine().isBefore(LocalDate.now())) {
                rent.setStatus(RentStatusEnum.ATRASADO);
                rentRepository.save(rent);
            } else if (rent.getDevolutionDate() == null) {
                rent.setStatus(RentStatusEnum.ALUGADO);
                rentRepository.save(rent);
            }

        } else {

            if (rent.getDevolutionDate().isAfter(rent.getDeadLine())) {
                rent.setStatus(RentStatusEnum.ENTREGUE_COM_ATRASO);
                rentRepository.save(rent);
            } else {
                rent.setStatus(RentStatusEnum.NO_PRAZO);
                rentRepository.save(rent);
            }
        }
    }
}