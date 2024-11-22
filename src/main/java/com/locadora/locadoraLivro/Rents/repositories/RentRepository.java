package com.locadora.locadoraLivro.Rents.repositories;

import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RentRepository extends JpaRepository<RentModel, Integer> {
    boolean existsByBookIdAndStatus(int bookId, RentStatusEnum status);
    boolean existsByRenterIdAndStatus(int renterId, RentStatusEnum status);
    boolean existsByRenterIdAndBookIdAndStatus(int renterId, int bookId, RentStatusEnum status);
    List<RentModel> findAllByStatus(RentStatusEnum status);
    List<RentModel> findAllByRenterId(int renterId);
    List<RentModel> findAllByRenterIdAndStatus(int renterId, RentStatusEnum status);
    List<RentModel> findAllByBookId(int bookId);
}
