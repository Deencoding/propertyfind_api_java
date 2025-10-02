package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.UserEntity;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class UserRepository {

    private final JdbcTemplate jdbcTemplate;

    public UserRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // create
    public void save(UserEntity user) {
        String sql = "INSERT INTO users(full_name, email, password, phone_number, role) " +
                "VALUES(?,?,?,?,?)";
        jdbcTemplate.update(sql, user.getFullName(), user.getEmail(), user.getPassword(),
                user.getPhoneNumber(), user.getRole());
    }

    // read all
    public List<UserEntity> findAll() {
        String sql ="SELECT id," +
                    " full_name as fullName," +
                    " email, password," +
                    " phone_number as phoneNumber," +
                    "registered_date as registeredDate," +
                    " updated_at as updatedAt," +
                    " active, " +
                    "role" +
                    " FROM users ";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(UserEntity.class));
    }

    //  read one
    public Optional<UserEntity> findById(Long id) {
        String sql = "SELECT id, full_name as fullName, email, password, phone_number as phoneNumber," +
                "registered_date as registeredDate, updated_at as updatedAt, active, role FROM users " +
                "WHERE id = ? ";
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

    // read by email
    public Optional<UserEntity> findByEmail(String email) {
        String sql = "SELECT id, full_name as fullName, email, password, phone_number as phoneNumber," +
                "registered_date as registeredDate, updated_at as updatedAt, active, role FROM users " +
                "WHERE email = ? ";
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

    // update user
    public int update(UserEntity user) {
        String sql = "UPDATE users SET full_name = ?, email = ?, password = ?, phone_number = ?, active = ?, WHERE id = ?";
        return jdbcTemplate.update(sql,
                user.getFullName(),
                user.getEmail(),
                user.getPassword(),
                user.getPhoneNumber(),
                user.isActive()
        );
    }

    // delete
    public int delete(Long id) {
        String sql = "DELETE FROM users WHERE id = ?";
        return jdbcTemplate.update(sql, id);
    }

}
