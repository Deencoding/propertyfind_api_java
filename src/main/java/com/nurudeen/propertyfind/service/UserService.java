package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.UserMapper;
import com.nurudeen.propertyfind.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    // create
    public UserCreateResponseDto createUser(UserCreateDto dto){
        // check if email already exists in the db
        Optional< UserEntity> existingUser = userRepository.findByEmail(dto.getEmail());

        if(existingUser.isPresent()){
            throw new RuntimeException("Email already exists" + dto.getEmail());
        }

        UserEntity user = userMapper.toEntity(dto);
        userRepository.save(user);

        return userMapper.toCreateResponse(user); // map saved entity -> response DTO
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
    public UserUpdateResponseDto updateUser(Long id, UserUpdateDto dto){
     UserEntity user =  userRepository.findById(id)
             .orElseThrow(()-> new RuntimeException("User not found with id" + id));

     // update fields
     user.setFullName(dto.getFullName());
     user.setEmail(dto.getEmail());
     user.setPhoneNumber(dto.getPhoneNumber());
     user.setPassword(dto.getPassword());

     userRepository.update(user);

     return userMapper.toUpdateResponse(user); // map updated entity -> response DTO
    }

    // delete
    public void deleteUser(Long id){
        userRepository.delete(id);
    }
}
