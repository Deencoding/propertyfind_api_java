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

    // create user
    @PostMapping("/provider/{providerId}")
    public ResponseEntity<PropertyCreateResponseDto> createProperty(@PathVariable Long providerId,
                                                                    @RequestBody PropertyCreateDto dto) {
        PropertyCreateResponseDto response = propertyService.createProperty(providerId, dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // get all properties
    @GetMapping
    public ResponseEntity<List<PropertyResponseDto>> getAllProperties() {
        return ResponseEntity.ok(propertyService.getAllProperties());
    }

    // get property by id
    @GetMapping("/{id}")
    public ResponseEntity<PropertyResponseDto> getPropertyById(@PathVariable Long id) {
        return ResponseEntity.ok(propertyService.getPropertyById(id));
    }


    // update property
    @PutMapping("/{id}")
    public ResponseEntity<PropertyUpdateResponseDto> updateProperty(
            @PathVariable Long id,
            @RequestBody PropertyUpdateDto dto
    ) {
        PropertyUpdateResponseDto response = propertyService.updateProperty(id, dto);
        return ResponseEntity.ok(response);
    }

    // Delete property
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }
}

