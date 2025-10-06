package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.mappers.PropertyMapper;
import com.nurudeen.propertyfind.repository.PropertyRepository;

import java.util.List;

public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    // create
    public PropertyCreateResponseDto createProperty(PropertyCreateDto dto){
        PropertyEntity property = propertyMapper.toEntity(dto);
        propertyRepository.save(property);

        return propertyMapper.toCreateResponse(property); // map saved entity to response

    }

    // read all
    public List<PropertyResponseDto> getAllProperties(){
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    // read one
    public PropertyResponseDto getPropertyById(Long id){
        return propertyRepository.findById(id)
                .map(propertyMapper::toResponse)
                .orElseThrow(() -> new RuntimeException("property not found with id" + id));
    }

    // update
    public PropertyUpdateResponseDto updateProperty(Long id, PropertyUpdateDto dto){
        PropertyEntity property = propertyRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("property not found with id" + id));

        // update fields
        property.setTitle(dto.getTitle());
        property.setDescription(dto.getDescription());
        property.setArea(dto.getArea());
        property.setAddress(dto.getAddress());
        property.setAvailable(dto.isAvailable());
        property.setBathroom(dto.getBathroom());
        property.setBedroom(dto.getBedroom());
        property.setCity(dto.getCity());
        property.setImageUrls(dto.getImageUrls());
        property.setCountry(dto.getCountry());
        property.setPricePerYear(dto.getPricePerYear());

        propertyRepository.update(property);

        return propertyMapper.toUpdateResponse(property); // map updated entity

    }

    // delete
    public void deleteProperty(Long id){
        propertyRepository.delete(id);
    }


}
