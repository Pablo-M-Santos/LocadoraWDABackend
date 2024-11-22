package com.locadora.locadoraLivro.Rents.repositories;

import com.locadora.locadoraLivro.Rents.models.RentModel;
import com.locadora.locadoraLivro.Rents.models.RentStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RentRepository extends JpaRepository<RentModel, Integer> {
    boolean existsByBookIdAndStatus(int bookId, RentStatusEnum status);
    boolean existsByRenterIdAndStatus(int renterId, RentStatusEnum status);
    boolean existsByRenterIdAndBookIdAndStatus(int renterId, int bookId, RentStatusEnum status);
    List<RentModel> findAllByStatus(RentStatusEnum status);
    List<RentModel> findAllByRenterId(int renterId);
    List<RentModel> findAllByRenterIdAndStatus(int renterId, RentStatusEnum status);
    List<RentModel> findAllByBookId(int bookId);
    List<RentModel> findAllByBookIdAndStatus(int bookId, RentStatusEnum status);

    @Query("SELECT u FROM RentModel u " +
            "JOIN u.renter r " +
            "JOIN u.book b " +
            "WHERE LOWER(REPLACE(r.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(b.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%'))")
    List<RentModel> findAllByRenterNameOrBookName(@Param("search") String search, Sort sort);

    @Query("SELECT u FROM RentModel u " +
            "JOIN u.renter r " +
            "JOIN u.book b " +
            "WHERE LOWER(REPLACE(r.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(b.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:search, ' ', ''), '%'))")
    Page<RentModel> findAllByRenterNameOrBookName(@Param("search") String search, Pageable pageable);

    @Query("SELECT u FROM RentModel u WHERE LOWER(u.status) = LOWER(:status)")
    Page<RentModel> findAllByStatus(@Param("status") String status, Pageable pageable);

    @Query("SELECT u FROM RentModel u " +
            "JOIN u.renter r " +
            "JOIN u.book b " +
            "WHERE (LOWER(FUNCTION('REPLACE', r.name, ' ', '')) LIKE LOWER(CONCAT('%', FUNCTION('REPLACE', :search, ' ', ''), '%')) " +
            "OR LOWER(FUNCTION('REPLACE', b.name, ' ', '')) LIKE LOWER(CONCAT('%', FUNCTION('REPLACE', :search, ' ', ''), '%'))) " +
            "AND LOWER(u.status) = LOWER(:status)")
    Page<RentModel> findAllByRenterNameOrBookNameAndStatus(@Param("search") String search, @Param("status") String status, Pageable pageable);

}
