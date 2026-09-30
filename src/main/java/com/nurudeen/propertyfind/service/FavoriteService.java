package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.property.PropertyResponseDto;
import com.nurudeen.propertyfind.entity.FavoriteEntity;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.exception.DuplicateResourceException;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.PropertyMapper;
import com.nurudeen.propertyfind.repository.FavoriteRepository;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.util.SecurityUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyMapper propertyMapper;

    public FavoriteService(FavoriteRepository favoriteRepository, PropertyRepository propertyRepository, PropertyMapper propertyMapper) {
        this.favoriteRepository = favoriteRepository;
        this.propertyRepository = propertyRepository;
        this.propertyMapper = propertyMapper;
    }

    public void addFavorite(Long propertyId) {
        Long userId = SecurityUtils.getCurrentUser().getId();

        // Check if property exists
        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + propertyId));

        if (favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new DuplicateResourceException("Property is already in favorites");
        }

        FavoriteEntity favorite = new FavoriteEntity(userId, propertyId, LocalDateTime.now());
        favoriteRepository.save(favorite);
    }

    public void removeFavorite(Long propertyId) {
        Long userId = SecurityUtils.getCurrentUser().getId();
        
        if (!favoriteRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new ResourceNotFoundException("Property is not in favorites");
        }
        
        favoriteRepository.delete(userId, propertyId);
    }

    public List<PropertyResponseDto> getUserFavorites() {
        Long userId = SecurityUtils.getCurrentUser().getId();
        
        List<PropertyEntity> properties = favoriteRepository.findFavoritePropertiesByUserId(userId);
        
        return properties.stream()
                .map(propertyMapper::toResponse)
                .toList();
    }
}
