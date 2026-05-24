package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.mappers.PropertyEntityRowMapper;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class PropertyRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public PropertyRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Create
    public void save(PropertyEntity property) {
        String sql = """
                INSERT INTO properties (
                    description, title, address, city, state, country,
                    price_per_year, bedroom, bathroom, area, image_urls,
                    available, listed_date, updated_at, provider_id
                ) VALUES (
                    :description, :title, :address, :city, :state, :country,
                    :pricePerYear, :bedroom, :bathroom, :area, :imageUrls,
                    :available, :listedDate, :updatedAt, :providerId
                )
                """;

        String[] imageUrls = property.getImageUrls() != null
                ? property.getImageUrls().toArray(new String[0])
                : new String[0];

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("description", property.getDescription(), Types.VARCHAR)
                .addValue("title", property.getTitle(), Types.VARCHAR)
                .addValue("address", property.getAddress(), Types.VARCHAR)
                .addValue("city", property.getCity(), Types.VARCHAR)
                .addValue("state", property.getState(), Types.VARCHAR)
                .addValue("country", property.getCountry(), Types.VARCHAR)
                .addValue("pricePerYear", property.getPricePerYear(), Types.DECIMAL)
                .addValue("bedroom", property.getBedroom(), Types.INTEGER)
                .addValue("bathroom", property.getBathroom(), Types.INTEGER)
                .addValue("area", property.getArea(), Types.DOUBLE)
                .addValue("imageUrls", imageUrls, Types.ARRAY)
                .addValue("available", property.isAvailable(), Types.BOOLEAN)
                .addValue("listedDate", Timestamp.valueOf(property.getListedDate()), Types.TIMESTAMP)
                .addValue("updatedAt", Timestamp.valueOf(property.getUpdatedAt()), Types.TIMESTAMP)
                .addValue("providerId", property.getProviderId(), Types.BIGINT);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        property.setId(keyHolder.getKey().longValue());
    }

    // Read all
    public List<PropertyEntity> findAll() {
        String sql = """
                SELECT id, description, title, address, city, state, country,
                       price_per_year AS pricePerYear, bedroom, bathroom, area,
                       image_urls, available, listed_date AS listedDate,
                       updated_at AS updatedAt, provider_id AS providerId
                FROM properties
                """;
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), new PropertyEntityRowMapper());
    }

    // Read one by ID
    public Optional<PropertyEntity> findById(Long id) {
        String sql = """
                SELECT id, description, title, address, city, state, country,
                       price_per_year AS pricePerYear, bedroom, bathroom, area,
                       image_urls, available, listed_date AS listedDate,
                       updated_at AS updatedAt, provider_id AS providerId
                FROM properties
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id, Types.BIGINT);

        try {
            PropertyEntity property = namedParameterJdbcTemplate.queryForObject(sql, params, new PropertyEntityRowMapper());
            return Optional.ofNullable(property);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Read all by provider ID
    public List<PropertyEntity> findByProviderId(Long providerId) {
        String sql = """
                SELECT id, description, title, address, city, state, country,
                       price_per_year AS pricePerYear, bedroom, bathroom, area,
                       image_urls, available, listed_date AS listedDate,
                       updated_at AS updatedAt, provider_id AS providerId
                FROM properties
                WHERE provider_id = :providerId
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("providerId", providerId, Types.BIGINT);

        return namedParameterJdbcTemplate.query(sql, params, new PropertyEntityRowMapper());
    }

    // Update — updated_at is set here in Java before persisting
    public void update(PropertyEntity property) {
        property.setUpdatedAt(LocalDateTime.now());

        String sql = """
                UPDATE properties
                SET description    = :description,
                    title          = :title,
                    address        = :address,
                    city           = :city,
                    state          = :state,
                    country        = :country,
                    price_per_year = :pricePerYear,
                    bedroom        = :bedroom,
                    bathroom       = :bathroom,
                    area           = :area,
                    image_urls     = :imageUrls,
                    available      = :available,
                    updated_at     = :updatedAt,
                    provider_id    = :providerId
                WHERE id = :id
                """;

        String[] imageUrls = property.getImageUrls() != null
                ? property.getImageUrls().toArray(new String[0])
                : new String[0];

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("description", property.getDescription(), Types.VARCHAR)
                .addValue("title", property.getTitle(), Types.VARCHAR)
                .addValue("address", property.getAddress(), Types.VARCHAR)
                .addValue("city", property.getCity(), Types.VARCHAR)
                .addValue("state", property.getState(), Types.VARCHAR)
                .addValue("country", property.getCountry(), Types.VARCHAR)
                .addValue("pricePerYear", property.getPricePerYear(), Types.DECIMAL)
                .addValue("bedroom", property.getBedroom(), Types.INTEGER)
                .addValue("bathroom", property.getBathroom(), Types.INTEGER)
                .addValue("area", property.getArea(), Types.DOUBLE)
                .addValue("imageUrls", imageUrls, Types.ARRAY)
                .addValue("available", property.isAvailable(), Types.BOOLEAN)
                .addValue("updatedAt", Timestamp.valueOf(property.getUpdatedAt()), Types.TIMESTAMP)
                .addValue("providerId", property.getProviderId(), Types.BIGINT)
                .addValue("id", property.getId(), Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }

    // Delete
    public void delete(Long id) {
        String sql = "DELETE FROM properties WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id, Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
