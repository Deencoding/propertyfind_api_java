package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.user.UserResponseDto;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

   private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    // get all users
    @GetMapping("get-all-users")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

}
