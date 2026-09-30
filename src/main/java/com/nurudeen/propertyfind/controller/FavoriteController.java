package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.property.PropertyResponseDto;
import com.nurudeen.propertyfind.service.FavoriteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping("/{propertyId}")
    public ResponseEntity<String> addFavorite(@PathVariable Long propertyId) {
        favoriteService.addFavorite(propertyId);
        return ResponseEntity.status(HttpStatus.CREATED).body("Property added to favorites");
    }

    @DeleteMapping("/{propertyId}")
    public ResponseEntity<String> removeFavorite(@PathVariable Long propertyId) {
        favoriteService.removeFavorite(propertyId);
        return ResponseEntity.ok("Property removed from favorites");
    }

    @GetMapping
    public ResponseEntity<List<PropertyResponseDto>> getUserFavorites() {
        return ResponseEntity.ok(favoriteService.getUserFavorites());
    }
}
