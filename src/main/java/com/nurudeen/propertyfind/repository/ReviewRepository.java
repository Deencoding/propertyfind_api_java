package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.dto.review.RatingSummaryResponseDto;
import com.nurudeen.propertyfind.entity.ReviewEntity;
import com.nurudeen.propertyfind.mappers.ReviewEntityRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Optional;

@Repository
public class ReviewRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public ReviewRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(ReviewEntity review) {
        String sql = """
                INSERT INTO reviews (user_id, property_id, rating, comment, created_at, updated_at)
                VALUES (:userId, :propertyId, :rating, :comment, :createdAt, :updatedAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", review.getUserId(), Types.BIGINT)
                .addValue("propertyId", review.getPropertyId(), Types.BIGINT)
                .addValue("rating", review.getRating(), Types.INTEGER)
                .addValue("comment", review.getComment(), Types.VARCHAR)
                .addValue("createdAt", Timestamp.valueOf(review.getCreatedAt()), Types.TIMESTAMP)
                .addValue("updatedAt", Timestamp.valueOf(review.getUpdatedAt()), Types.TIMESTAMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        review.setId(keyHolder.getKey().longValue());
    }

    public List<ReviewEntity> findByPropertyId(Long propertyId) {
        String sql = "SELECT * FROM reviews WHERE property_id = :propertyId ORDER BY created_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("propertyId", propertyId, Types.BIGINT);
        return namedParameterJdbcTemplate.query(sql, params, new ReviewEntityRowMapper());
    }

    public Optional<ReviewEntity> findById(Long id) {
        String sql = "SELECT * FROM reviews WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id, Types.BIGINT);
        List<ReviewEntity> results = namedParameterJdbcTemplate.query(sql, params, new ReviewEntityRowMapper());
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public void delete(Long id) {
        String sql = "DELETE FROM reviews WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id, Types.BIGINT);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public boolean existsByUserIdAndPropertyId(Long userId, Long propertyId) {
        String sql = "SELECT COUNT(*) FROM reviews WHERE user_id = :userId AND property_id = :propertyId";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId, Types.BIGINT)
                .addValue("propertyId", propertyId, Types.BIGINT);
        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
    public void update(ReviewEntity review) {
        String sql = """
                UPDATE reviews SET rating = :rating, comment = :comment, updated_at = :updatedAt
                WHERE id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", review.getId(), Types.BIGINT)
                .addValue("rating", review.getRating(), Types.INTEGER)
                .addValue("comment", review.getComment(), Types.VARCHAR)
                .addValue("updatedAt", Timestamp.valueOf(review.getUpdatedAt()), Types.TIMESTAMP);
        namedParameterJdbcTemplate.update(sql, params);
    }

    public RatingSummaryResponseDto getPropertyRating(Long propertyId) {
        String sql = """
                SELECT COALESCE(ROUND(AVG(rating), 2), 0) AS average_rating, COUNT(*) AS review_count
                FROM reviews WHERE property_id = :propertyId
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("propertyId", propertyId, Types.BIGINT);
        return namedParameterJdbcTemplate.queryForObject(sql, params, (rs, rowNum) -> {
            RatingSummaryResponseDto dto = new RatingSummaryResponseDto();
            dto.setAverageRating(rs.getBigDecimal("average_rating"));
            dto.setReviewCount(rs.getLong("review_count"));
            return dto;
        });
    }
}
