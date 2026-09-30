package com.nurudeen.propertyfind.mappers;

import java.sql.Timestamp;
import com.nurudeen.propertyfind.entity.FavoriteEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class FavoriteEntityRowMapper implements RowMapper<FavoriteEntity> {
    @Override
    public FavoriteEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        FavoriteEntity favorite = new FavoriteEntity();
        favorite.setId(rs.getLong("id"));
        favorite.setUserId(rs.getLong("user_id"));
        favorite.setPropertyId(rs.getLong("property_id"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) {
            favorite.setCreatedAt(createdAt.toLocalDateTime());
        }
        
        return favorite;
    }
}
