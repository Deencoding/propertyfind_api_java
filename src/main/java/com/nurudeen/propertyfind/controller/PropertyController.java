package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.PaginatedResponseDto;
import com.nurudeen.propertyfind.service.FileStorageService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.service.PropertyService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/properties")
public class PropertyController {

    private final PropertyService propertyService;
    private final FileStorageService fileStorageService;

    public PropertyController(PropertyService propertyService, FileStorageService fileStorageService) {
        this.propertyService = propertyService;
        this.fileStorageService = fileStorageService;
    }

    // create property
    @PostMapping
    public ResponseEntity<PropertyCreateResponseDto> createProperty(@Valid @RequestBody PropertyCreateDto dto) {
        PropertyCreateResponseDto response = propertyService.createProperty(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    // get all properties by provider id
    @GetMapping("/provider/{providerId}")
    public ResponseEntity<List<PropertyResponseDto>> getPropertyByProviderId(@PathVariable Long providerId) {
        return ResponseEntity.ok(propertyService.getAllPropertiesByProviderId(providerId));
    }

    // get all properties
    @GetMapping("/all")
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
            @Valid @RequestBody PropertyUpdateDto dto
    ) {
        PropertyUpdateResponseDto response = propertyService.updateProperty(id, dto);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<PropertyResponseDto> updateStatus(
            @PathVariable Long id, @Valid @RequestBody PropertyStatusUpdateDto dto) {
        return ResponseEntity.ok(propertyService.updateStatus(id, dto));
    }

    // Delete property by id
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProperty(@PathVariable Long id) {
        propertyService.deleteProperty(id);
        return ResponseEntity.ok("Property deleted successfully");
    }

    // search properties
    @PostMapping("/search")
    public ResponseEntity<PaginatedResponseDto<PropertyResponseDto>> searchProperties(
            @RequestBody PropertySearchDto searchDto) {
        return ResponseEntity.ok(propertyService.searchProperties(searchDto));
    }

    // upload image for a property
    @PostMapping("/{id}/images")
    public ResponseEntity<String> uploadPropertyImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file,
            HttpServletRequest request) {
            
        String fileName = fileStorageService.storeFile(file);
        
        // Build URL
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/uploads/")
                .path(fileName)
                .toUriString();
                
        propertyService.addImageToProperty(id, fileDownloadUri);
        
        return ResponseEntity.ok(fileDownloadUri);
    }
}

