package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.entity.ReviewEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

public class ReviewEntityRowMapper implements RowMapper<ReviewEntity> {
    @Override
    public ReviewEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        ReviewEntity review = new ReviewEntity();
        review.setId(rs.getLong("id"));
        review.setUserId(rs.getLong("user_id"));
        review.setPropertyId(rs.getLong("property_id"));
        review.setRating(rs.getInt("rating"));
        review.setComment(rs.getString("comment"));

        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) review.setCreatedAt(createdAt.toLocalDateTime());

        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) review.setUpdatedAt(updatedAt.toLocalDateTime());

        return review;
    }
}
