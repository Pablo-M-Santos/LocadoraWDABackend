package com.locadora.locadoraLivro.Rents.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Renters.models.RenterModel;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tb_rents")
public class RentModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "renter_id", nullable = false)
    private RenterModel renter;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private BookModel book;

    private LocalDate devolutionDate;

    @Column(nullable = false)
    private LocalDate deadLine;

    @Column(nullable = false)
    private LocalDate rentDate;

    @Enumerated(EnumType.STRING)
    @Column(length = 25, nullable = false)
    private RentStatusEnum status;

    public RentModel(RenterModel renter, BookModel book, LocalDate deadLine){
        this.renter = renter;
        this.book = book;
        this.deadLine = deadLine;
        this.rentDate = LocalDate.now();
        this.status = null;
    }
}