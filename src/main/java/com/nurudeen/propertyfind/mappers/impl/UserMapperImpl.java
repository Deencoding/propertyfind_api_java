package com.nurudeen.propertyfind.mappers.impl;

import com.nurudeen.propertyfind.dto.user.UserCreateDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.Mapper;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;


@Component
public class UserMapperImpl implements Mapper<UserEntity, UserCreateDto> {

    private ModelMapper modelMapper;

    @Override
    public UserCreateDto mapTo(UserEntity userEntity) {
        return modelMapper.map(userEntity, UserCreateDto.class);
    }

    @Override
    public UserEntity mapFrom(UserCreateDto userCreateDto) {
        return modelMapper.map(userCreateDto, UserEntity.class);
    }
}
