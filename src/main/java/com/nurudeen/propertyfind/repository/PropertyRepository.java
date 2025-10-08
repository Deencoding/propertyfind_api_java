package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.util.JdbcUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Array;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class PropertyRepository {

    private final JdbcTemplate jdbcTemplate;

    public PropertyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // Create
    public void save(PropertyEntity property) {
        String sql = "INSERT INTO properties (" +
                "description, title, address, city, state, country, " +
                "price_per_year, bedroom, bathroom, area, image_urls, " +
                "available, listed_date, updated_at, provider_id" +
                ") VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) RETURNING id";

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                property.getDescription(),
                property.getTitle(),
                property.getAddress(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getPricePerYear().longValue(),
                property.getBedroom(),
                property.getBathroom(),
                property.getArea(),
                property.getImageUrls() != null ? property.getImageUrls().toArray(new String[0]) : new String[0],
                property.isAvailable(),
                Timestamp.valueOf(property.getListedDate()),
                Timestamp.valueOf(property.getUpdatedAt()),
                property.getProviderId()
        );

        property.setId(id);
    }




    // Read all
    public List<PropertyEntity> findAll() {
        String sql = "SELECT " +
                "id, description, title, address, city, state, country, " +
                "price_per_year as pricePerYear, bedroom, bathroom, area, " +
                "image_urls, available, listed_date as listedDate, " +
                "updated_at as updatedAt, provider_id as providerId " +
                "FROM properties";

        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            PropertyEntity property = new PropertyEntity();
            property.setId(rs.getLong("id"));
            property.setDescription(rs.getString("description"));
            property.setTitle(rs.getString("title"));
            property.setAddress(rs.getString("address"));
            property.setCity(rs.getString("city"));
            property.setState(rs.getString("state"));
            property.setCountry(rs.getString("country"));
            property.setPricePerYear(rs.getBigDecimal("pricePerYear"));
            property.setBedroom(rs.getInt("bedroom"));
            property.setBathroom(rs.getInt("bathroom"));
            property.setArea(rs.getDouble("area"));

            // Use utility method for PostgreSQL arrays
            property.setImageUrls(JdbcUtils.pgArrayToList(rs.getArray("image_urls")));


            property.setAvailable(rs.getBoolean("available"));
            property.setListedDate(rs.getTimestamp("listedDate").toLocalDateTime());
            property.setUpdatedAt(rs.getTimestamp("updatedAt") != null ?
                    rs.getTimestamp("updatedAt").toLocalDateTime() : null);
            property.setProviderId(rs.getLong("providerId"));

            return property;
        });
    }


    // Read one
    public Optional<PropertyEntity> findById(Long id) {
        String sql = "SELECT " +
                "id, description, title, address, city, state, country, " +
                "price_per_year as pricePerYear, bedroom, bathroom, area, " +
                "image_urls, available, listed_date as listedDate, " +
                "updated_at as updatedAt, provider_id as providerId " +
                "FROM properties WHERE id = ?";

        try {
            PropertyEntity property = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(PropertyEntity.class),
                    id
            );
            return Optional.ofNullable(property);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Read by provider ID
    public List<PropertyEntity> findByProviderId(Long providerId) {
        String sql = "SELECT " +
                "id, description, title, address, city, state, country, " +
                "price_per_year as pricePerYear, bedroom, bathroom, area, " +
                "image_urls, available, listed_date as listedDate, " +
                "updated_at as updatedAt, provider_id as providerId " +
                "FROM properties WHERE provider_id = ?";

        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PropertyEntity.class), providerId);
    }

    // Update
    public void update(PropertyEntity property) {
        String sql = "UPDATE properties SET " +
                "description = ?, title = ?, address = ?, city = ?, state = ?, country = ?, " +
                "price_per_year = ?, bedroom = ?, bathroom = ?, area = ?, image_urls = ?, " +
                "available = ?, updated_at = ?, provider_id = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                property.getDescription(),
                property.getTitle(),
                property.getAddress(),
                property.getCity(),
                property.getState(),
                property.getCountry(),
                property.getPricePerYear(),
                property.getBedroom(),
                property.getBathroom(),
                property.getArea(),
                String.join(",", property.getImageUrls()),
                property.isAvailable(),
                property.getUpdatedAt(),
                property.getProviderId(),
                property.getId()
        );
    }

    // Delete
    public void delete(Long id) {
        String sql = "DELETE FROM properties WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
