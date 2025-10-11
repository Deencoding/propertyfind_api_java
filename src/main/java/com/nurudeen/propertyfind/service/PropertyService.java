package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.dto.user.UserResponseDto;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.PropertyMapper;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PropertyService {

    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;
    private final UserRepository userRepository;

    public PropertyService(PropertyRepository propertyRepository, PropertyMapper propertyMapper, UserRepository userRepository) {
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
        this.userRepository = userRepository;
    }

    // create
    public PropertyCreateResponseDto createProperty(Long providerId, PropertyCreateDto dto) {
        PropertyEntity property = propertyMapper.toEntity(dto);

        // Fetch provider
        UserEntity provider = userRepository.findById(providerId)
                .orElseThrow(() -> new RuntimeException("Provider not found with id " + providerId));

        LocalDateTime now = LocalDateTime.now();
        property.setListedDate(now);
        property.setUpdatedAt(now);

        // Save property
        propertyRepository.save(property);

        // Map to response
        return propertyMapper.toCreateResponse(property);
    }



    // read all
    public List<PropertyResponseDto> getAllProperties(){
        return propertyRepository.findAll()
                .stream()
                .map(propertyMapper::toResponse)
                .toList();
    }

    // read all properties by provider id
    public List<PropertyResponseDto> getAllPropertiesByProviderId(Long providerId) {
        List<PropertyEntity> properties = propertyRepository.findByProviderId(providerId);

        if (properties.isEmpty()) {
            throw new RuntimeException("No properties found for provider with id " + providerId);
        }

        return properties.stream()
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

        // fetch existing property
        PropertyEntity property = propertyRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("property not found with id" + id));

        // update only non-null fields from dto
        if (dto.getTitle() != null) property.setTitle(dto.getTitle());
        if (dto.getDescription() != null) property.setDescription(dto.getDescription());
        if (dto.getArea() != null) property.setArea(dto.getArea());
        if (dto.getAddress() != null) property.setAddress(dto.getAddress());
        if (dto.isAvailable() != null) property.setAvailable(dto.isAvailable());
        if (dto.getBathroom() != null) property.setBathroom(dto.getBathroom());
        if (dto.getBedroom() != null) property.setBedroom(dto.getBedroom());
        if (dto.getCity() != null) property.setCity(dto.getCity());
        if (dto.getImageUrls() != null) property.setImageUrls(dto.getImageUrls());
        if (dto.getCountry() != null) property.setCountry(dto.getCountry());
        if (dto.getPricePerYear() != null) property.setPricePerYear(dto.getPricePerYear());
        // update the updateAt timestamp
        property.setUpdatedAt(LocalDateTime.now());

        // persist updates
        propertyRepository.update(property);

        return propertyMapper.toUpdateResponse(property); // map updated entity

    }

    // delete
    public void deleteProperty(Long id) {
        // first verify the property exists
        propertyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Property not found with id " + id));

        // delete
        propertyRepository.delete(id);
    }

}
