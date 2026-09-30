package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.admin.AnalyticsDashboardResponseDto;
import com.nurudeen.propertyfind.repository.AdminAnalyticsRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
public class AdminAnalyticsService {

    private final AdminAnalyticsRepository adminAnalyticsRepository;

    public AdminAnalyticsService(AdminAnalyticsRepository adminAnalyticsRepository) {
        this.adminAnalyticsRepository = adminAnalyticsRepository;
    }

    public AnalyticsDashboardResponseDto getDashboardAnalytics() {
        return adminAnalyticsRepository.getDashboardAnalytics();
    }
}
