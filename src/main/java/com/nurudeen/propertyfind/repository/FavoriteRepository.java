package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.FavoriteEntity;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.mappers.FavoriteEntityRowMapper;
import com.nurudeen.propertyfind.mappers.PropertyEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
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
public class FavoriteRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public FavoriteRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(FavoriteEntity favorite) {
        String sql = """
                INSERT INTO favorites (user_id, property_id, created_at)
                VALUES (:userId, :propertyId, :createdAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", favorite.getUserId(), Types.BIGINT)
                .addValue("propertyId", favorite.getPropertyId(), Types.BIGINT)
                .addValue("createdAt", Timestamp.valueOf(favorite.getCreatedAt()), Types.TIMESTAMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        favorite.setId(keyHolder.getKey().longValue());
    }

    public void delete(Long userId, Long propertyId) {
        String sql = "DELETE FROM favorites WHERE user_id = :userId AND property_id = :propertyId";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId, Types.BIGINT)
                .addValue("propertyId", propertyId, Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public boolean existsByUserIdAndPropertyId(Long userId, Long propertyId) {
        String sql = "SELECT COUNT(*) FROM favorites WHERE user_id = :userId AND property_id = :propertyId";
        
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId, Types.BIGINT)
                .addValue("propertyId", propertyId, Types.BIGINT);

        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }

    public List<PropertyEntity> findFavoritePropertiesByUserId(Long userId) {
        String sql = """
                SELECT p.id, p.description, p.title, p.address, p.city, p.state, p.country,
                       p.price_per_year AS pricePerYear, p.bedroom, p.bathroom, p.area,
                       p.image_urls, p.available, p.status, p.listed_date AS listedDate,
                       p.updated_at AS updatedAt, p.provider_id AS providerId
                FROM properties p
                INNER JOIN favorites f ON p.id = f.property_id
                WHERE f.user_id = :userId
                ORDER BY f.created_at DESC
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("userId", userId, Types.BIGINT);

        return namedParameterJdbcTemplate.query(sql, params, new PropertyEntityRowMapper());
    }
}
