package com.nurudeen.propertyfind.controller;


import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users") // all endpoints start with api/users
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    // create user
    @PostMapping
    public ResponseEntity<UserCreateResponseDto> createUser(@RequestBody UserCreateDto dto){
        UserCreateResponseDto response = userService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // get all users
    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // get one user by id
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@PathVariable Long id){
        UserResponseDto user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    // update user
    @PutMapping("/{id}")
    public ResponseEntity<UserUpdateResponseDto> updateUser(@PathVariable Long id, @RequestBody UserUpdateDto dto){
        UserUpdateResponseDto response = userService.updateUser(id, dto);
        return ResponseEntity.ok(response);
    }

    // delete user
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
         return ResponseEntity.ok("User deleted successfully");
    }

}
