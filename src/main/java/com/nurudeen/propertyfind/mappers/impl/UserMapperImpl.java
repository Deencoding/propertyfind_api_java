package com.nurudeen.propertyfind.mappers.impl;

import com.nurudeen.propertyfind.dto.user.UserCreateDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.Mapper;


public class UserMapperImpl implements Mapper<UserEntity, UserCreateDto> {

    private ModelMapper modelMapper;

    @Override
    public UserCreateDto mapTo(UserEntity userEntity) {
        return null;
    }

    @Override
    public UserEntity mapFrom(UserCreateDto userCreateDto) {
        return null;
    }
}
