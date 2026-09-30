package com.nurudeen.propertyfind.repository;

import java.math.BigDecimal;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.PropertyStatus;
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
                    available, status, listed_date, updated_at, provider_id
                ) VALUES (
                    :description, :title, :address, :city, :state, :country,
                    :pricePerYear, :bedroom, :bathroom, :area, :imageUrls,
                    :available, :status, :listedDate, :updatedAt, :providerId
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
                .addValue("status", property.getStatus().name(), Types.VARCHAR)
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
                       image_urls, available, status, listed_date AS listedDate,
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
                       image_urls, available, status, listed_date AS listedDate,
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
                       image_urls, available, status, listed_date AS listedDate,
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
                    status         = :status,
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
                .addValue("status", property.getStatus().name(), Types.VARCHAR)
                .addValue("updatedAt", Timestamp.valueOf(property.getUpdatedAt()), Types.TIMESTAMP)
                .addValue("providerId", property.getProviderId(), Types.BIGINT)
                .addValue("id", property.getId(), Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public void updateStatus(PropertyEntity property) {
        String sql = """
                UPDATE properties SET status = :status, available = :available, updated_at = :updatedAt
                WHERE id = :id
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", property.getId(), Types.BIGINT)
                .addValue("status", property.getStatus().name(), Types.VARCHAR)
                .addValue("available", property.isAvailable(), Types.BOOLEAN)
                .addValue("updatedAt", Timestamp.valueOf(property.getUpdatedAt()), Types.TIMESTAMP);
        namedParameterJdbcTemplate.update(sql, params);
    }

    // Delete
    public void delete(Long id) {
        String sql = "DELETE FROM properties WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id, Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }
    // Search properties with filters and pagination
    public List<PropertyEntity> search(
            String keyword, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minBedroom, Integer minBathroom, Boolean available,
            String city, String state, PropertyStatus status,
            int limit, int offset, String sortBy, String sortDirection) {

        StringBuilder sql = new StringBuilder("""
                SELECT id, description, title, address, city, state, country,
                       price_per_year AS pricePerYear, bedroom, bathroom, area,
                       image_urls, available, status, listed_date AS listedDate,
                       updated_at AS updatedAt, provider_id AS providerId
                FROM properties
                WHERE 1=1
                """);

        MapSqlParameterSource params = new MapSqlParameterSource();
        buildSearchCriteria(sql, params, keyword, minPrice, maxPrice, minBedroom, minBathroom, available, city, state, status);

        // Add sorting (validate input to prevent SQL injection)
        String safeSortBy = validateSortColumn(sortBy);
        String safeDirection = sortDirection.equalsIgnoreCase("ASC") ? "ASC" : "DESC";
        sql.append(" ORDER BY ").append(safeSortBy).append(" ").append(safeDirection);

        // Add pagination
        sql.append(" LIMIT :limit OFFSET :offset");
        params.addValue("limit", limit);
        params.addValue("offset", offset);

        return namedParameterJdbcTemplate.query(sql.toString(), params, new PropertyEntityRowMapper());
    }

    // Count for pagination
    public long countSearch(
            String keyword, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minBedroom, Integer minBathroom, Boolean available,
            String city, String state, PropertyStatus status) {

        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM properties WHERE 1=1 ");
        MapSqlParameterSource params = new MapSqlParameterSource();
        buildSearchCriteria(sql, params, keyword, minPrice, maxPrice, minBedroom, minBathroom, available, city, state, status);

        Long count = namedParameterJdbcTemplate.queryForObject(sql.toString(), params, Long.class);
        return count != null ? count : 0L;
    }

    private void buildSearchCriteria(
            StringBuilder sql, MapSqlParameterSource params,
            String keyword, BigDecimal minPrice, BigDecimal maxPrice,
            Integer minBedroom, Integer minBathroom, Boolean available,
            String city, String state, PropertyStatus status) {

        if (keyword != null && !keyword.trim().isEmpty()) {
            sql.append(" AND (LOWER(title) LIKE :keyword OR LOWER(description) LIKE :keyword OR LOWER(city) LIKE :keyword OR LOWER(state) LIKE :keyword OR LOWER(country) LIKE :keyword)");
            params.addValue("keyword", "%" + keyword.toLowerCase() + "%");
        }
        if (minPrice != null) {
            sql.append(" AND price_per_year >= :minPrice");
            params.addValue("minPrice", minPrice);
        }
        if (maxPrice != null) {
            sql.append(" AND price_per_year <= :maxPrice");
            params.addValue("maxPrice", maxPrice);
        }
        if (minBedroom != null) {
            sql.append(" AND bedroom >= :minBedroom");
            params.addValue("minBedroom", minBedroom);
        }
        if (minBathroom != null) {
            sql.append(" AND bathroom >= :minBathroom");
            params.addValue("minBathroom", minBathroom);
        }
        if (status != null) {
            sql.append(" AND status = :status");
            params.addValue("status", status.name(), Types.VARCHAR);
        }
        if (available != null) {
            sql.append(" AND available = :available");
            params.addValue("available", available);
        }
        if (city != null && !city.trim().isEmpty()) {
            sql.append(" AND LOWER(city) = :city");
            params.addValue("city", city.toLowerCase());
        }
        if (state != null && !state.trim().isEmpty()) {
            sql.append(" AND LOWER(state) = :state");
            params.addValue("state", state.toLowerCase());
        }
    }

    private String validateSortColumn(String sortBy) {
        return switch (sortBy.toLowerCase()) {
            case "price" -> "price_per_year";
            case "bedroom" -> "bedroom";
            case "bathroom" -> "bathroom";
            case "area" -> "area";
            case "city" -> "city";
            case "state" -> "state";
            case "listed_date" -> "listed_date";
            default -> "listed_date"; // Default safe fallback
        };
    }
}
