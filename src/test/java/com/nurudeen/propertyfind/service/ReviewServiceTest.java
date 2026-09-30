package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.review.RatingSummaryResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewCreateDto;
import com.nurudeen.propertyfind.dto.review.ReviewResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewUpdateDto;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.ReviewEntity;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.exception.DuplicateResourceException;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.ReviewMapper;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.repository.ReviewRepository;
import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import com.nurudeen.propertyfind.util.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.access.AccessDeniedException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Spy
    private ReviewMapper reviewMapper = new ReviewMapper(new ModelMapper());

    @InjectMocks
    private ReviewService reviewService;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockUser = new UserEntity();
        mockUser.setId(1L);
        CustomUserPrincipal principal = new CustomUserPrincipal(mockUser);

        mockedSecurityUtils = mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);
    }

    @AfterEach
    void tearDown() {
        if (mockedSecurityUtils != null) {
            mockedSecurityUtils.close();
        }
    }

    @Test
    void addReview_Success() {
        Long propertyId = 100L;
        ReviewCreateDto dto = new ReviewCreateDto();
        dto.setRating(5);
        dto.setComment("Great place");

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(new PropertyEntity()));
        when(reviewRepository.existsByUserIdAndPropertyId(mockUser.getId(), propertyId)).thenReturn(false);

        ReviewResponseDto result = reviewService.addReview(propertyId, dto);

        assertNotNull(result);
        assertEquals(5, result.getRating());
        assertEquals("Great place", result.getComment());
        verify(reviewRepository, times(1)).save(any(ReviewEntity.class));
    }

    @Test
    void addReview_Duplicate_ThrowsException() {
        Long propertyId = 100L;
        ReviewCreateDto dto = new ReviewCreateDto();

        when(propertyRepository.findById(propertyId)).thenReturn(Optional.of(new PropertyEntity()));
        when(reviewRepository.existsByUserIdAndPropertyId(mockUser.getId(), propertyId)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> reviewService.addReview(propertyId, dto));
        verify(reviewRepository, never()).save(any(ReviewEntity.class));
    }

    @Test
    void addReview_ConcurrentDuplicate_ThrowsConflict() {
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(new PropertyEntity()));
        doThrow(new DuplicateKeyException("duplicate")).when(reviewRepository).save(any());
        ReviewCreateDto dto = new ReviewCreateDto();
        dto.setRating(4);
        assertThrows(DuplicateResourceException.class, () -> reviewService.addReview(100L, dto));
    }

    @Test
    void missingProperty_ThrowsNotFound() {
        assertThrows(ResourceNotFoundException.class, () -> reviewService.getPropertyReviews(100L));
        assertThrows(ResourceNotFoundException.class, () -> reviewService.getPropertyRating(100L));
        assertThrows(ResourceNotFoundException.class, () -> reviewService.addReview(100L, new ReviewCreateDto()));
        verifyNoInteractions(reviewRepository);
    }

    @Test
    void updateReview_UpdatesRatingAndComment() {
        ReviewEntity review = new ReviewEntity();
        review.setId(10L);
        review.setUserId(1L);
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        ReviewUpdateDto dto = new ReviewUpdateDto();
        dto.setRating(3);
        dto.setComment("Updated review");
        ReviewResponseDto result = reviewService.updateReview(10L, dto);
        assertEquals(3, result.getRating());
        assertEquals("Updated review", result.getComment());
        assertNotNull(review.getUpdatedAt());
        mockedSecurityUtils.verify(() -> SecurityUtils.checkAccess(1L));
        verify(reviewRepository).update(review);
    }

    @Test
    void updateAndDelete_OtherUser_Forbidden() {
        ReviewEntity review = new ReviewEntity();
        review.setUserId(2L);
        when(reviewRepository.findById(10L)).thenReturn(Optional.of(review));
        mockedSecurityUtils.when(() -> SecurityUtils.checkAccess(2L))
                .thenThrow(new AccessDeniedException("Forbidden"));
        assertThrows(AccessDeniedException.class, () -> reviewService.updateReview(10L, new ReviewUpdateDto()));
        assertThrows(AccessDeniedException.class, () -> reviewService.deleteReview(10L));
        verify(reviewRepository, never()).update(any());
        verify(reviewRepository, never()).delete(any());
    }

    @Test
    void getPropertyRating_ReturnsSummary() {
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(new PropertyEntity()));
        RatingSummaryResponseDto summary = new RatingSummaryResponseDto();
        when(reviewRepository.getPropertyRating(100L)).thenReturn(summary);
        assertSame(summary, reviewService.getPropertyRating(100L));
    }
}
