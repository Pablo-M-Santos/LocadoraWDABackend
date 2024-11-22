package com.locadora.locadoraLivro.dashboard.service;

import com.locadora.locadoraLivro.Books.repositories.BookRepository;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import com.locadora.locadoraLivro.Renters.repositories.RenterRepository;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import com.locadora.locadoraLivro.Rents.repositories.RentRepository;
import com.locadora.locadoraLivro.dashboard.DTOs.BooksMoreRented;
import com.locadora.locadoraLivro.dashboard.DTOs.RentsperRenterResponseDTO;
import com.locadora.locadoraLivro.dashboard.mappers.BookRentMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DashboardServices {

    @Autowired
    RentRepository rentRepository;

    @Autowired
    RenterRepository renterRepository;

    @Autowired
    BookRepository bookRepository;

    @Autowired
    private  BookRentMapper bookRentMapper;

    public int getNumberOfRentals(){
        List<RentModel> totalRents = rentRepository.findAll();
        int rentsQuantity = totalRents.size();

        return rentsQuantity;
    }

    public int getNumberOfRentalsLate(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.ATRASADO);
        int rentsLate = totalRentsLate.size();

        return rentsLate;
    }

    public int getDeliveredInTime(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.NO_PRAZO);
        int rentsInTime = totalRentsLate.size();

        return rentsInTime;
    }

    public int getDeliveredWithDelay(){
        List<RentModel> totalRentsLate = rentRepository.findAllByStatus(RentStatusEnum.ENTREGUE_COM_ATRASO);
        int rentsWithDelay = totalRentsLate.size();

        return rentsWithDelay;
    }

    public List<RentsperRenterResponseDTO> getRentsPerRenter() {
        List<RenterModel> renters = renterRepository.findAll();
        List<RentsperRenterResponseDTO> renterRentList = new ArrayList<>();

        for (RenterModel renter : renters) {
            List<RentModel> rents = rentRepository.findAllByRenterId(renter.getId());
            List<RentModel> rentsActive = rentRepository.findAllByRenterIdAndStatus(renter.getId(), RentStatusEnum.ALUGADO);
            renterRentList.add(new RentsperRenterResponseDTO(renter.getName(), rents.size(), rentsActive.size()));
        }

        return renterRentList;
    }

    public List<BooksMoreRented> getBooksMoreRented() {
        return bookRentMapper.toBooksMoreRentedList(bookRepository.findAll());
    }
}
