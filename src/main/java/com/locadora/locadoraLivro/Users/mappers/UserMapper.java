package com.locadora.locadoraLivro.Users.mappers;

import com.locadora.locadoraLivro.Users.DTOs.UserResponseDTO;
import com.locadora.locadoraLivro.Users.models.UserModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UserMapper {

    public List<UserResponseDTO> toUserResponseList(List<UserModel> userList){
        return userList.stream().map(this::toUserResponse).toList();
    }

    public UserResponseDTO toUserResponse(UserModel model){
        return UserResponseDTO
                .builder()
                .id(model.getId())
                .name(model.getName())
                .email(model.getEmail())
                .role(model.getRole())
                .build();
    }
}
