package com.nurudeen.propertyfind.mappers;


import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.entity.UserEntity;
import org.modelmapper.ModelMapper;


public class UserMapper {

    private final ModelMapper modelMapper;

    public UserMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // Create
    public UserEntity toEntity(UserCreateDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserCreateResponseDto toCreateResponse(UserEntity entity) {
        return modelMapper.map(entity, UserCreateResponseDto.class);
    }

    // Update
    public UserEntity toEntity(UserUpdateDto dto) {
        return modelMapper.map(dto, UserEntity.class);
    }

    public UserUpdateResponseDto toUpdateResponse(UserEntity entity) {
        return modelMapper.map(entity, UserUpdateResponseDto.class);
    }

    // response
    public UserResponseDto toResponse(UserEntity entity){
        return modelMapper.map(entity, UserResponseDto.class);
    }

}

