package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.dto.admin.AnalyticsDashboardResponseDto;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Map;

@Repository
public class AdminAnalyticsRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public AdminAnalyticsRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public AnalyticsDashboardResponseDto getDashboardAnalytics() {
        AnalyticsDashboardResponseDto dto = new AnalyticsDashboardResponseDto();

        // Users stats
        String userSql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN role = 'HOME_SEEKER' THEN 1 ELSE 0 END) as seekers,
                SUM(CASE WHEN role = 'HOME_PROVIDER' THEN 1 ELSE 0 END) as providers
            FROM users
        """;
        Map<String, Object> userStats = jdbcTemplate.queryForMap(userSql, new MapSqlParameterSource());
        dto.setTotalUsers(getLongValue(userStats.get("total")));
        dto.setTotalHomeSeekers(getLongValue(userStats.get("seekers")));
        dto.setTotalProviders(getLongValue(userStats.get("providers")));

        // Properties stats
        String propSql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN available = true THEN 1 ELSE 0 END) as available_props
            FROM properties
        """;
        Map<String, Object> propStats = jdbcTemplate.queryForMap(propSql, new MapSqlParameterSource());
        dto.setTotalProperties(getLongValue(propStats.get("total")));
        dto.setTotalAvailableProperties(getLongValue(propStats.get("available_props")));

        // Bookings stats
        String bookSql = """
            SELECT 
                COUNT(*) as total,
                SUM(CASE WHEN status = 'PENDING' THEN 1 ELSE 0 END) as pending,
                SUM(CASE WHEN status = 'APPROVED' THEN 1 ELSE 0 END) as approved,
                SUM(CASE WHEN status = 'CANCELLED' THEN 1 ELSE 0 END) as cancelled
            FROM bookings
        """;
        Map<String, Object> bookStats = jdbcTemplate.queryForMap(bookSql, new MapSqlParameterSource());
        dto.setTotalBookings(getLongValue(bookStats.get("total")));
        dto.setPendingBookings(getLongValue(bookStats.get("pending")));
        dto.setApprovedBookings(getLongValue(bookStats.get("approved")));
        dto.setCancelledBookings(getLongValue(bookStats.get("cancelled")));

        // Reviews stats
        String reviewSql = """
            SELECT 
                COUNT(*) as total,
                AVG(rating) as avg_rating
            FROM reviews
        """;
        Map<String, Object> reviewStats = jdbcTemplate.queryForMap(reviewSql, new MapSqlParameterSource());
        dto.setTotalReviews(getLongValue(reviewStats.get("total")));
        
        Object avg = reviewStats.get("avg_rating");
        if (avg instanceof Number n) {
            dto.setAveragePropertyRating(Math.round(n.doubleValue() * 100.0) / 100.0);
        } else {
            dto.setAveragePropertyRating(0.0);
        }

        return dto;
    }

    private long getLongValue(Object value) {
        if (value instanceof Number n) {
            return n.longValue();
        }
        return 0L;
    }
}
