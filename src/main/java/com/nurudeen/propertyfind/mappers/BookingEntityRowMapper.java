package com.nurudeen.propertyfind.mappers;

import java.sql.Timestamp;
import com.nurudeen.propertyfind.entity.BookingEntity;
import com.nurudeen.propertyfind.entity.BookingStatus;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BookingEntityRowMapper implements RowMapper<BookingEntity> {
    @Override
    public BookingEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        BookingEntity booking = new BookingEntity();
        booking.setId(rs.getLong("id"));
        booking.setPropertyId(rs.getLong("property_id"));
        booking.setHomeSeekerId(rs.getLong("home_seeker_id"));
        
        Timestamp scheduledDate = rs.getTimestamp("scheduled_date");
        if (scheduledDate != null) booking.setScheduledDate(scheduledDate.toLocalDateTime());
        
        booking.setStatus(BookingStatus.valueOf(rs.getString("status")));
        booking.setMessage(rs.getString("message"));
        
        Timestamp createdAt = rs.getTimestamp("created_at");
        if (createdAt != null) booking.setCreatedAt(createdAt.toLocalDateTime());
        
        Timestamp updatedAt = rs.getTimestamp("updated_at");
        if (updatedAt != null) booking.setUpdatedAt(updatedAt.toLocalDateTime());
        
        return booking;
    }
}
