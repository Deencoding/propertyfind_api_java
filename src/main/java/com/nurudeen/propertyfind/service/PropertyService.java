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


        property.setProviderId(providerId);

        LocalDateTime now = LocalDateTime.now();
        property.setListedDate(now);

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
