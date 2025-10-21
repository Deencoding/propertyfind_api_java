package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId(), user.getRole().name());


        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setRegisteredDate(user.getRegisteredDate());
        response.setToken(token);
        response.setRefreshToken(refreshToken);

        return response;
    }
}
