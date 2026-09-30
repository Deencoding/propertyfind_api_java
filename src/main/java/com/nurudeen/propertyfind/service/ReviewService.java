package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.review.RatingSummaryResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewCreateDto;
import com.nurudeen.propertyfind.dto.review.ReviewResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewUpdateDto;
import com.nurudeen.propertyfind.entity.ReviewEntity;
import com.nurudeen.propertyfind.exception.DuplicateResourceException;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.ReviewMapper;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.repository.ReviewRepository;
import com.nurudeen.propertyfind.util.SecurityUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final PropertyRepository propertyRepository;
    private final ReviewMapper reviewMapper;

    public ReviewService(ReviewRepository reviewRepository, PropertyRepository propertyRepository, ReviewMapper reviewMapper) {
        this.reviewMapper = reviewMapper;
        this.reviewRepository = reviewRepository;
        this.propertyRepository = propertyRepository;
    }

    public ReviewResponseDto addReview(Long propertyId, ReviewCreateDto dto) {
        Long userId = SecurityUtils.getCurrentUser().getId();

        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + propertyId));

        if (reviewRepository.existsByUserIdAndPropertyId(userId, propertyId)) {
            throw new DuplicateResourceException("You have already reviewed this property");
        }

        ReviewEntity review = new ReviewEntity();
        review.setUserId(userId);
        review.setPropertyId(propertyId);
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setCreatedAt(LocalDateTime.now());
        review.setUpdatedAt(LocalDateTime.now());

        try {
            reviewRepository.save(review);
        } catch (DuplicateKeyException ex) {
            throw new DuplicateResourceException("You have already reviewed this property");
        }

        return reviewMapper.toResponse(review);
    }

    public List<ReviewResponseDto> getPropertyReviews(Long propertyId) {
        requireProperty(propertyId);
        return reviewRepository.findByPropertyId(propertyId).stream()
                .map(reviewMapper::toResponse)
                .collect(Collectors.toList());
    }

    public void deleteReview(Long reviewId) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));

        SecurityUtils.checkAccess(review.getUserId());
        reviewRepository.delete(reviewId);
    }

    public ReviewResponseDto updateReview(Long reviewId, ReviewUpdateDto dto) {
        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review not found with id " + reviewId));
        SecurityUtils.checkAccess(review.getUserId());
        review.setRating(dto.getRating());
        review.setComment(dto.getComment());
        review.setUpdatedAt(LocalDateTime.now());
        reviewRepository.update(review);
        return reviewMapper.toResponse(review);
    }

    public RatingSummaryResponseDto getPropertyRating(Long propertyId) {
        requireProperty(propertyId);
        return reviewRepository.getPropertyRating(propertyId);
    }

    private void requireProperty(Long propertyId) {
        propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + propertyId));
    }
}
