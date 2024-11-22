package com.locadora.locadoraLivro.Rents.mappers;

import com.locadora.locadoraLivro.Rents.DTOs.RentResponseDTO;
import com.locadora.locadoraLivro.Rents.models.RentModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class RentMapper {

    public List<RentResponseDTO> toRentResponseList(List<RentModel> rentList) {
        return rentList.stream().map(this::toRentResponse).toList();
    }

    public RentResponseDTO toRentResponse(RentModel model) {
        return RentResponseDTO
                .builder()
                .id(model.getId())
                .book(model.getBook())
                .renter(model.getRenter())
                .deadLine(model.getDeadLine())
                .devolutionDate(model.getDevolutionDate())
                .rentDate(model.getRentDate())
                .status(model.getStatus())
                .build();
    }
}
