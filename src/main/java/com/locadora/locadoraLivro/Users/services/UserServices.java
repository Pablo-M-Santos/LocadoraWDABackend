package com.locadora.locadoraLivro.Users.services;

import com.locadora.locadoraLivro.Exceptions.ModelNotFoundException;
import com.locadora.locadoraLivro.Users.DTOs.CreateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UpdateUserRequestDTO;
import com.locadora.locadoraLivro.Users.DTOs.UserResponseDTO;
import com.locadora.locadoraLivro.Users.Validation.UserValidation;
import com.locadora.locadoraLivro.Users.mappers.UserMapper;
import com.locadora.locadoraLivro.Users.models.PasswordResetToken;
import com.locadora.locadoraLivro.Users.models.UserModel;
import com.locadora.locadoraLivro.Users.repositories.PasswordResetTokenRepository;
import com.locadora.locadoraLivro.Users.repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @Autowired
    private UserValidation userEmailValidation;

    @Autowired
    private PasswordResetTokenRepository resetTokenRepository;

    public ResponseEntity<Void> create(@Valid CreateUserRequestDTO data) {
        userEmailValidation.validateEmail(data);

        String encryptedPassword = passwordEncoder.encode(data.password());
        UserModel newUser = new UserModel(data.name(), data.email(), encryptedPassword, data.role());
        userRepository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    public List<UserModel> findAll(String search) {
        if (Objects.equals(search, "")){
            List<UserModel> users = userRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
            if (users.isEmpty()) throw new ModelNotFoundException();
            return users;
        } else {
            List<UserModel> userByName = userRepository.findAllByName(search);
            return userByName;
        }
    }

    public Optional<UserModel> findById(int id) {
        return userRepository.findById(id);
    }

    public ResponseEntity<UserResponseDTO> update(int id, @Valid UpdateUserRequestDTO updateUserRequestDTO) {
        Optional<UserModel> response = userRepository.findById(id);
        if (response.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        var userModel = response.get();
        BeanUtils.copyProperties(updateUserRequestDTO, userModel, "password");


        if (updateUserRequestDTO.password() != null && !updateUserRequestDTO.password().isBlank()) {
            String encryptedPassword = passwordEncoder.encode(updateUserRequestDTO.password());
            userModel.setPassword(encryptedPassword);
        }

        userRepository.save(userModel);
        UserResponseDTO userResponseDTO = userMapper.toUserResponse(userModel);
        return ResponseEntity.status(HttpStatus.OK).body(userResponseDTO);
    }


    public ResponseEntity<Object> delete(int id){
        Optional<UserModel> response = userRepository.findById(id);
        if(response.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.delete(response.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");
    }

    public String createPasswordResetToken(String email) {
        Optional<UserModel> userOptional = Optional.ofNullable(userRepository.findByEmail(email));
        if (!userOptional.isPresent()) {
            return null;
        }

        UserModel user = userOptional.get();

        PasswordResetToken existingToken = resetTokenRepository.findByUser(user);
        if (existingToken != null) {
            if (existingToken.isExpired()) {
                resetTokenRepository.delete(existingToken);
            } else {
                return existingToken.getToken();
            }
        }

        String token = UUID.randomUUID().toString();
        PasswordResetToken newToken = new PasswordResetToken(token, user);
        resetTokenRepository.save(newToken);

        return token;
    }


    public boolean validatePasswordResetToken(String token) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token);
        if (resetToken == null) {
            return false;
        }

        if (resetToken.isExpired()) {
            return false;
        }

        return true;
    }


    public boolean resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = resetTokenRepository.findByToken(token);

        if (resetToken == null || resetToken.isExpired()) {
            return false;
        }

        UserModel user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        resetTokenRepository.delete(resetToken);

        return true;
    }


    public String getUserNameByEmail(String email) {
        UserModel user = userRepository.findByEmail(email);
        if (user != null) {
            return user.getName();
        }
        return null;
    }

}
