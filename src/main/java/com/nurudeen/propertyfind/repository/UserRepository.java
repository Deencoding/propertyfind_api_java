package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.mappers.UserEntityRowMapper;
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
import java.util.Objects;
import java.util.Optional;

@Repository
public class UserRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public UserRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    // Insert user and return saved entity with ID
    public void save(UserEntity user) {
        String sql = """
            INSERT INTO users (full_name, email, password, phone_number, role, registered_date, updated_at)
            VALUES (:fullName, :email, :password, :phoneNumber, :role, :registeredDate, :updatedAt)
            """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fullName", user.getFullName(), Types.VARCHAR)
                .addValue("email", user.getEmail(), Types.VARCHAR)
                .addValue("password", user.getPassword(), Types.VARCHAR)
                .addValue("phoneNumber", user.getPhoneNumber(), Types.VARCHAR)
                .addValue("role", user.getRole().name(), Types.VARCHAR)
                .addValue("registeredDate", Timestamp.valueOf(user.getRegisteredDate()), Types.TIMESTAMP)
                .addValue("updatedAt", Timestamp.valueOf(user.getUpdatedAt()), Types.TIMESTAMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        user.setId(Objects.requireNonNull(keyHolder.getKey()).longValue());
    }

    // Read all
    public List<UserEntity> findAll() {
        String sql = """
                SELECT id, full_name, email, password, phone_number,
                       registered_date, updated_at, active, role
                FROM users
                """;
        return namedParameterJdbcTemplate.query(sql, new MapSqlParameterSource(), new UserEntityRowMapper());
    }

    // Read by ID
    public Optional<UserEntity> findById(Long id) {
        String sql = """
                SELECT id, full_name, email, password, phone_number,
                       registered_date, updated_at, active, role
                FROM users
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id, Types.BIGINT);

        try {
            UserEntity user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserEntityRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Read by email
    public Optional<UserEntity> findByEmail(String email) {
        String sql = """
                SELECT id, full_name, email, password, phone_number,
                       registered_date, updated_at, active, role
                FROM users
                WHERE email = :email
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("email", email, Types.VARCHAR);

        try {
            UserEntity user = namedParameterJdbcTemplate.queryForObject(sql, params, new UserEntityRowMapper());
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // Update — updated_at is set here in Java before persisting
    public void update(UserEntity user) {
        user.setUpdatedAt(LocalDateTime.now());

        String sql = """
                UPDATE users
                SET full_name    = :fullName,
                    email        = :email,
                    password     = :password,
                    phone_number = :phoneNumber,
                    active       = :active,
                    role         = :role,
                    updated_at   = :updatedAt
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("fullName", user.getFullName(), Types.VARCHAR)
                .addValue("email", user.getEmail(), Types.VARCHAR)
                .addValue("password", user.getPassword(), Types.VARCHAR)
                .addValue("phoneNumber", user.getPhoneNumber(), Types.VARCHAR)
                .addValue("active", user.isActive(), Types.BOOLEAN)
                .addValue("role", user.getRole().name(), Types.VARCHAR)
                .addValue("updatedAt", Timestamp.valueOf(user.getUpdatedAt()), Types.TIMESTAMP)
                .addValue("id", user.getId(), Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }

    // Delete
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = :id";

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("id", id, Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }
}
