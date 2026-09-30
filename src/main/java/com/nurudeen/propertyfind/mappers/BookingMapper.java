package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.dto.booking.BookingResponseDto;
import com.nurudeen.propertyfind.entity.BookingEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper {

    private final ModelMapper modelMapper;

    public BookingMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public BookingResponseDto toResponse(BookingEntity entity) {
        return modelMapper.map(entity, BookingResponseDto.class);
    }
}
