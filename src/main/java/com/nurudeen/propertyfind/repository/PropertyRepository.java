package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

public class PropertyRepository {

    public JdbcTemplate jdbcTemplate;

    public PropertyRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // create
    public void save(PropertyEntity property) {
        String sql = "INSERT INTO properties (description, title, address, city, state, country, " +
                "price_per_year, bedroom, bathroom, area, image_urls, provider_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

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
                property.getImageUrls().toArray(new String[0]), // map List<String> → String[]
                property.getProvider() != null ? property.getProvider().getId() : null
        );

    }

    // read all
    public List<PropertyEntity> findAll() {
        String sql = "SELECT id, description, title, address, city, state, country, " +
                "price_per_year AS pricePerYear, bedroom, bathroom, area, image_urls AS imageUrls, " +
                "available, active, listed_date AS listedDate, updated_at AS updatedAt, provider_id " +
                "FROM properties";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(PropertyEntity.class));
    }

    // read one
    public Optional<PropertyEntity> findById(Long id) {
        String sql = "SELECT id, description, title, address, city, state, country, " +
                "price_per_year AS pricePerYear, bedroom, bathroom, area, image_urls AS imageUrls, " +
                "available, active, listed_date AS listedDate, updated_at AS updatedAt, provider_id " +
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

    // update
    public int update(PropertyEntity property) {
        String sql = "UPDATE properties SET description = ?, title = ?, address = ?, city = ?, state = ?, " +
                "country = ?, price_per_year = ?, bedroom = ?, bathroom = ?, area = ?, image_urls = ?, " +
                "available = ?, WHERE id = ?";
        return jdbcTemplate.update(sql,
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
                property.getImageUrls().toArray(new String[0]),
                property.isAvailable(),
                property.getProvider() != null ? property.getProvider().getId() : null,
                property.getId()
        );
    }

    // delete
    public int delete(Long id) {
        String sql = "DELETE FROM properties WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }
}

