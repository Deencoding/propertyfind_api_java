package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.entity.PropertyEntity;


import org.modelmapper.ModelMapper;

public class PropertyMapper {

    private final ModelMapper modelMapper;


    public PropertyMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    // create
    public PropertyEntity toEntity(PropertyCreateDto dto){
        return modelMapper.map(dto, PropertyEntity.class);
    }

    public PropertyCreateResponseDto toCreateResponse(PropertyEntity entity){
        return modelMapper.map(entity, PropertyCreateResponseDto.class);
    }


    // update
    public PropertyEntity toEntity(PropertyUpdateDto dto){
        return modelMapper.map(dto, PropertyEntity.class);
    }

    public PropertyUpdateResponseDto toUpdateResponse(PropertyEntity entity){
        return modelMapper.map(entity, PropertyUpdateResponseDto.class);
    }

    // response
    public PropertyResponseDto toResponse(PropertyEntity entity){
        return modelMapper.map(entity, PropertyResponseDto.class);
    }

}
