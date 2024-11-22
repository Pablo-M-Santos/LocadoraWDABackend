package com.locadora.locadoraLivro.Books.repositories;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<BookModel, Integer> {
    BookModel findByName(String name);
    List<BookModel> findAllByNameAndIsDeletedFalse(String name);
    List<BookModel> findByPublisherId(int publisherId);
    Page<BookModel> findAllByIsDeletedFalse(Pageable pageable);

    @Query("SELECT u FROM BookModel u WHERE " +
            "(LOWER(REPLACE(u.name, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%')) " +
            "OR LOWER(REPLACE(u.author, ' ', '')) LIKE LOWER(CONCAT('%', REPLACE(:searchTerm, ' ', ''), '%'))) " +
            "AND u.isDeleted = false")
    Page<BookModel> findAllByName(@Param("searchTerm") String searchTerm, Pageable pageable);
}