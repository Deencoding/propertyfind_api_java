package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.admin.AnalyticsDashboardResponseDto;
import com.nurudeen.propertyfind.repository.AdminAnalyticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class AdminAnalyticsServiceTest {

    @Mock
    private AdminAnalyticsRepository adminAnalyticsRepository;

    @InjectMocks
    private AdminAnalyticsService adminAnalyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDashboardAnalytics_Success() {
        AnalyticsDashboardResponseDto mockDto = new AnalyticsDashboardResponseDto();
        mockDto.setTotalUsers(50);
        mockDto.setTotalHomeSeekers(40);
        mockDto.setTotalProviders(10);
        mockDto.setTotalProperties(100);
        mockDto.setTotalAvailableProperties(85);
        mockDto.setTotalBookings(200);
        mockDto.setPendingBookings(50);
        mockDto.setApprovedBookings(130);
        mockDto.setCancelledBookings(20);
        mockDto.setTotalReviews(75);
        mockDto.setAveragePropertyRating(4.5);

        when(adminAnalyticsRepository.getDashboardAnalytics()).thenReturn(mockDto);

        AnalyticsDashboardResponseDto result = adminAnalyticsService.getDashboardAnalytics();

        assertNotNull(result);
        assertEquals(50, result.getTotalUsers());
        assertEquals(40, result.getTotalHomeSeekers());
        assertEquals(10, result.getTotalProviders());
        assertEquals(100, result.getTotalProperties());
        assertEquals(85, result.getTotalAvailableProperties());
        assertEquals(200, result.getTotalBookings());
        assertEquals(50, result.getPendingBookings());
        assertEquals(130, result.getApprovedBookings());
        assertEquals(20, result.getCancelledBookings());
        assertEquals(75, result.getTotalReviews());
        assertEquals(4.5, result.getAveragePropertyRating());
    }
}
