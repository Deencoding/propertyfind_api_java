package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.review.RatingSummaryResponseDto;
import com.nurudeen.propertyfind.dto.review.ReviewResponseDto;
import com.nurudeen.propertyfind.exception.GlobalExceptionHandler;
import com.nurudeen.propertyfind.service.ReviewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReviewControllerTest {

    private ReviewService reviewService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        reviewService = mock(ReviewService.class);
        mockMvc = MockMvcBuilders.standaloneSetup(new ReviewController(reviewService))
                .setControllerAdvice(new GlobalExceptionHandler()).build();
    }

    @Test
    void createAndUpdate_RejectInvalidRatingsAndLongComments() throws Exception {
        for (String body : new String[]{"{}", "{\"rating\":0}", "{\"rating\":6}",
                "{\"rating\":5,\"comment\":\"" + "a".repeat(1001) + "\"}"}) {
            mockMvc.perform(post("/api/reviews/property/100")
                    .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
            mockMvc.perform(put("/api/reviews/10")
                    .contentType(MediaType.APPLICATION_JSON).content(body))
                    .andExpect(status().isBadRequest());
        }
        verifyNoInteractions(reviewService);
    }

    @Test
    void create_RatingWithoutComment_ReturnsCreated() throws Exception {
        ReviewResponseDto response = new ReviewResponseDto();
        response.setRating(5);
        when(reviewService.addReview(eq(100L), any())).thenReturn(response);
        mockMvc.perform(post("/api/reviews/property/100")
                .contentType(MediaType.APPLICATION_JSON).content("{\"rating\":5}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.rating").value(5));
    }

    @Test
    void update_ReturnsUpdatedReview() throws Exception {
        ReviewResponseDto response = new ReviewResponseDto();
        response.setRating(1);
        when(reviewService.updateReview(eq(10L), any())).thenReturn(response);
        mockMvc.perform(put("/api/reviews/10")
                .contentType(MediaType.APPLICATION_JSON).content("{\"rating\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rating").value(1));
    }

    @Test
    void rating_ReturnsAverageAndCount() throws Exception {
        RatingSummaryResponseDto response = new RatingSummaryResponseDto();
        response.setAverageRating(new BigDecimal("4.50"));
        response.setReviewCount(2);
        when(reviewService.getPropertyRating(100L)).thenReturn(response);
        mockMvc.perform(get("/api/reviews/property/100/rating"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.averageRating").value(4.5))
                .andExpect(jsonPath("$.reviewCount").value(2));
    }
}
