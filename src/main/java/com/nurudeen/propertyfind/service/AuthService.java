package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.JwtService;
import com.nurudeen.propertyfind.util.PasswordUtils;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordUtils passwordUtils, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        //  Find user by email
        UserEntity user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        //  Compare plain password with hashed password
        if (!passwordUtils.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        //  Generate JWT token
        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());

        // Build response
        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setRegisteredDate(user.getRegisteredDate());
        response.setToken(token);

        return response;
    }
}
