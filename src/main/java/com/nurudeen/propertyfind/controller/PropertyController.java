package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.service.PropertyService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/properties")
public class PropertyController {

    private final PropertyService propertyService;

    public PropertyController(PropertyService propertyService) {
        this.propertyService = propertyService;
    }

    // create property
    @PostMapping("/provider/{providerId}")
    public ResponseEntity<PropertyCreateResponseDto> createProperty(@PathVariable Long providerId,
                                                                    @RequestBody PropertyCreateDto dto) {
        PropertyCreateResponseDto response = propertyService.createProperty(providerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // get all properties by provider id
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<PropertyResponseDto>> getPropertyByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(propertyService.getAllPropertiesByProviderId(providerId));
    }

    // get all properties
    @GetMapping("get-all-properties")
    public ResponseEntity<List<PropertyResponseDto>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    // get property by property id
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }


    // update property by id
    @PutMapping("/{id}")
    public ResponseEntity<PropertyUpdateResponseDto> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyUpdateDto dto
    ) {
        PropertyUpdateResponseDto response = propertyService.updateProperty(id, dto);
        return ResponseEntity.ok(response);
    }

    // Delete property by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }
}

