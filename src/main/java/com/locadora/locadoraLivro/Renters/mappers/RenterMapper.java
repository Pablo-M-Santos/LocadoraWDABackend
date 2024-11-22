package com.locadora.locadoraLivro.Renters.mappers;

import com.locadora.locadoraLivro.Renters.DTOs.RenterResponseDTO;
import com.locadora.locadoraLivro.Renters.models.RenterModel;
import lombok.Getter;
import org.springframework.stereotype.Component;

import java.util.List;

@Getter
@Component
public class RenterMapper {

    public  List<RenterResponseDTO> toRenterResponseList(List<RenterModel> renterList){
        return renterList.stream().map(this::toRenterResponse).toList();
    }

    public RenterResponseDTO toRenterResponse(RenterModel model){
        return RenterResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .telephone(model.getTelephone())
                .address(model.getAddress())
                .cpf(model.getCpf())
                .build();
    }
}
