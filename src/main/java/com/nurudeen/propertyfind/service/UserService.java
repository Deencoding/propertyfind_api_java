package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.UserMapper;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.util.PasswordUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordUtils passwordUtils;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordUtils passwordUtils) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordUtils = passwordUtils;
    }

    public UserCreateResponseDto createUser(UserCreateDto dto) {
        Optional<UserEntity> existingUser = userRepository.findByEmail(dto.getEmail());
        if (existingUser.isPresent()) {
            throw new RuntimeException("Email already exists: " + dto.getEmail());
        }

        UserEntity user = userMapper.toEntity(dto);

        LocalDateTime now = LocalDateTime.now();
        user.setRegisteredDate(now);
        user.setUpdatedAt(now);

        // hash password before saving
        String hashedPassword = passwordUtils.hashPassword(user.getPassword());
        user.setPassword(hashedPassword);

        // Save to DB
        userRepository.save(user);

        System.out.println("DEBUG: User before mapping => " + user);

        return userMapper.toCreateResponse(user);
    }


    // read all
    public List<UserResponseDto> getAllUsers(){
        return userRepository.findAll().stream().map(userMapper::toResponse).toList();
    }

    // read one
    public UserResponseDto getUserById(Long id){
        return userRepository.findById(id)
                .map(userMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("User not found with id" + id));
    }

    // update
    public UserUpdateResponseDto updateUser(Long id, UserUpdateDto dto) {
        // Fetch the existing user
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update only non-null fields from DTO
        if (dto.getFullName() != null) existingUser.setFullName(dto.getFullName());
        if (dto.getEmail() != null) existingUser.setEmail(dto.getEmail());
        if (dto.getPassword() != null) existingUser.setPassword(dto.getPassword());
        if (dto.getPhoneNumber() != null) existingUser.setPhoneNumber(dto.getPhoneNumber());

        // Update the updatedAt timestamp
        existingUser.setUpdatedAt(LocalDateTime.now());

        // Save changes in the database
        userRepository.update(existingUser);

        // Return the updated user mapped to the response DTO
        return userMapper.toUpdateResponse(existingUser);
    }


    // delete
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
