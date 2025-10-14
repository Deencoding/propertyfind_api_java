package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.dto.user.UserCreateDto;
import com.nurudeen.propertyfind.dto.user.UserCreateResponseDto;
import com.nurudeen.propertyfind.service.AuthService;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/auth")
public class AuthController {


    private final AuthService authService;
    private final UserService userService;

    public AuthController(AuthService authService, UserService userService) {
        this.authService = authService;
        this.userService = userService;
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

}
