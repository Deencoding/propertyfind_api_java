package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.dto.review.ReviewResponseDto;
import com.nurudeen.propertyfind.entity.ReviewEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {

    private final ModelMapper modelMapper;

    public ReviewMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public ReviewResponseDto toResponse(ReviewEntity entity) {
        return modelMapper.map(entity, ReviewResponseDto.class);
    }
}
