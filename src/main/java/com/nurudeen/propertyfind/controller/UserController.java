package com.nurudeen.propertyfind.controller;


import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // all endpoints start with api/users
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // get one user by id
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // update user
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto){
        UserUpdateResponseDto response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    // delete user
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
         return ResponseEntity.ok("User deleted successfully");
    }

}
