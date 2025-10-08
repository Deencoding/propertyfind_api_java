package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.util.JdbcUtils;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PropertyEntityRowMapper implements RowMapper<PropertyEntity> {
    @Override
    public PropertyEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
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

        // Convert PostgreSQL array to List<String>
        property.setImageUrls(JdbcUtils.pgArrayToList(rs.getArray("image_urls")));

        property.setAvailable(rs.getBoolean("available"));
        property.setListedDate(rs.getTimestamp("listedDate").toLocalDateTime());
        property.setUpdatedAt(rs.getTimestamp("updatedAt") != null ?
                rs.getTimestamp("updatedAt").toLocalDateTime() : null);
        property.setProviderId(rs.getLong("providerId"));

        return property;
    }
}
