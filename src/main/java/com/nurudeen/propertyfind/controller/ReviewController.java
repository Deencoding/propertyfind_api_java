package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.review.RatingSummaryResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewCreateDto;
import com.nurudeen.propertyfind.dto.review.ReviewResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewUpdateDto;
import com.nurudeen.propertyfind.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/property/{propertyId}")
    public ResponseEntity<ReviewResponseDto> addReview(
            @PathVariable Long propertyId,
            @Valid @RequestBody ReviewCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(reviewService.addReview(propertyId, dto));
    }

    @GetMapping("/property/{propertyId}")
    public ResponseEntity<List<ReviewResponseDto>> getPropertyReviews(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getPropertyReviews(propertyId));
    }

    @GetMapping("/property/{propertyId}/rating")
    public ResponseEntity<RatingSummaryResponseDto> getPropertyRating(@PathVariable Long propertyId) {
        return ResponseEntity.ok(reviewService.getPropertyRating(propertyId));
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewResponseDto> updateReview(
            @PathVariable Long reviewId,
            @Valid @RequestBody ReviewUpdateDto dto) {
        return ResponseEntity.ok(reviewService.updateReview(reviewId, dto));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<String> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok("Review deleted successfully");
    }
}
