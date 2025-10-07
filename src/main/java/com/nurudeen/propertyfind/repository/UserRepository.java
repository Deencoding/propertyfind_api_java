package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.UserEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // INSERT user and return saved entity with ID
    public void save(UserEntity user) {
        String sql = """
            INSERT INTO users (full_name, email, password, phone_number, role, registered_date, updated_at)
            VALUES (?, ?, ?, ?, ?, ?, ?)
            RETURNING id
        """;

        Long id = jdbcTemplate.queryForObject(
                sql,
                Long.class,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.getRole().name(),
                Timestamp.valueOf(user.getRegisteredDate()),
                Timestamp.valueOf(user.getUpdatedAt())
        );

        user.setId(id);
    }
    // READ ALL
    public List<UserEntity> findAll() {
        String sql = """
                SELECT id,
                       full_name AS fullName,
                       email,
                       password,
                       phone_number AS phoneNumber,
                       registered_date AS registeredDate,
                       updated_at AS updatedAt,
                       active,
                       role
                FROM users
                """;
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserEntity.class));
    }

    // READ ONE BY ID
    public Optional<UserEntity> findById(Long id) {
        String sql = """
                SELECT id,
                       full_name AS fullName,
                       email,
                       password,
                       phone_number AS phoneNumber,
                       registered_date AS registeredDate,
                       updated_at AS updatedAt,
                       active,
                       role
                FROM users
                WHERE id = ?
                """;
        try {
            UserEntity user = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(UserEntity.class),
                    id
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // READ ONE BY EMAIL
    public Optional<UserEntity> findByEmail(String email) {
        String sql = """
                SELECT id,
                       full_name AS fullName,
                       email,
                       password,
                       phone_number AS phoneNumber,
                       registered_date AS registeredDate,
                       updated_at AS updatedAt,
                       active,
                       role
                FROM users
                WHERE email = ?
                """;
        try {
            UserEntity user = jdbcTemplate.queryForObject(
                    sql,
                    new BeanPropertyRowMapper<>(UserEntity.class),
                    email
            );
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    // UPDATE
    public void update(UserEntity user) {
        String sql = """
                UPDATE users
                SET full_name = ?,
                    email = ?,
                    password = ?,
                    phone_number = ?,
                    active = ?,
                    role = ?
                WHERE id = ?
                """;
        jdbcTemplate.update(sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.isActive(),
                user.getRole().toString(),
                user.getId()
        );
    }

    // DELETE
    public void delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, id);
    }
}
