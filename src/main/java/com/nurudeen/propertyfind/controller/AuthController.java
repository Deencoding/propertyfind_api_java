package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.dto.user.UserCreateDto;
import com.nurudeen.propertyfind.dto.user.UserCreateResponseDto;
import com.nurudeen.propertyfind.security.JwtService;
import com.nurudeen.propertyfind.service.AuthService;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    private final AuthService authService;
    private final UserService userService;
    private final JwtService jwtService;

    public AuthController(AuthService authService, UserService userService, JwtService jwtService) {
        this.authService = authService;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto dto){
        LoginResponseDto response = authService.login(dto);
        return ResponseEntity.ok(response);
    }

    // create user
    @PostMapping("/register")
    public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateDto dto){
        UserCreateResponseDto response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // refresh token
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");
        if (refreshToken == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "Refresh token is missing"));
        }

        try {
            // Validate refresh token
            String email = jwtService.extractEmail(refreshToken);
            String role = jwtService.extractRole(refreshToken);
            Long userId = jwtService.extractUserId(refreshToken);

            if (!jwtService.isTokenExpired(refreshToken)) {
                String newAccessToken = jwtService.generateToken(email, userId, role);
                return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Refresh token expired"));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Invalid refresh token"));
        }
    }


}
