package com.locadora.locadoraLivro.Books.mappers;

import com.locadora.locadoraLivro.Books.models.BookModel;
import com.locadora.locadoraLivro.Books.DTOs.BookResponseDTO;
import com.locadora.locadoraLivro.Publishers.DTOs.PublisherResponseDTO;
import com.locadora.locadoraLivro.Publishers.mappers.PublisherMapper;
import com.locadora.locadoraLivro.Publishers.models.PublisherModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BookMapper {

    private final PublisherMapper publisherMapper;

    public BookMapper(PublisherMapper publisherMapper) {
        this.publisherMapper = publisherMapper;
    }

    public List<BookResponseDTO> toBookResponseList(List<BookModel> bookList){
        return bookList.stream().map(this::toBookResponse).toList();
    }

    public BookResponseDTO toBookResponse(BookModel model){
        PublisherResponseDTO publisherResponseDTO = publisherMapper.toPublisherResponse(model.getPublisher());

        return BookResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .author(model.getAuthor())
                .launchDate(model.getLaunchDate())
                .totalQuantity(model.getTotalQuantity())
                .publisher(model.getPublisher())
                .totalInUse(model.getTotalInUse())
                .build();
    }
}