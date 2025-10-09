package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    public UserRepository userRepository;

    public LoginResponseDto login(LoginRequestDto dto){
        // find user by email
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(()->new RuntimeException("invalid email"));

        if (!user.getPassword().equals(dto.getPassword())){
            throw new RuntimeException("invalid password");
        }

        // build response
        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setRegisteredDate(user.getRegisteredDate());

        return response;

    }
}
