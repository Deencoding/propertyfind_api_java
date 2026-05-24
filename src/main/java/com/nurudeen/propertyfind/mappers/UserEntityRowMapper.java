package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.entity.UserEnum;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class UserEntityRowMapper implements RowMapper<UserEntity> {

    @Override
    public UserEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        UserEntity user = new UserEntity();
        user.setId(rs.getLong("id"));
        user.setFullName(rs.getString("full_name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setPhoneNumber(rs.getString("phone_number"));
        user.setRegisteredDate(rs.getTimestamp("registered_date").toLocalDateTime());
        user.setUpdatedAt(rs.getTimestamp("updated_at") != null
                ? rs.getTimestamp("updated_at").toLocalDateTime()
                : null);
        user.setActive(rs.getBoolean("active"));
        user.setRole(UserEnum.valueOf(rs.getString("role")));
        return user;
    }
}
