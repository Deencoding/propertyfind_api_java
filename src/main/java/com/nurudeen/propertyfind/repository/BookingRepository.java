package com.nurudeen.propertyfind.repository;

import com.nurudeen.propertyfind.entity.BookingEntity;
import com.nurudeen.propertyfind.mappers.BookingEntityRowMapper;
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
public class BookingRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public BookingRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    public void save(BookingEntity booking) {
        String sql = """
                INSERT INTO bookings (property_id, home_seeker_id, scheduled_date, status, message, created_at, updated_at)
                VALUES (:propertyId, :homeSeekerId, :scheduledDate, :status, :message, :createdAt, :updatedAt)
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("propertyId", booking.getPropertyId(), Types.BIGINT)
                .addValue("homeSeekerId", booking.getHomeSeekerId(), Types.BIGINT)
                .addValue("scheduledDate", Timestamp.valueOf(booking.getScheduledDate()), Types.TIMESTAMP)
                .addValue("status", booking.getStatus().name(), Types.VARCHAR)
                .addValue("message", booking.getMessage(), Types.VARCHAR)
                .addValue("createdAt", Timestamp.valueOf(booking.getCreatedAt()), Types.TIMESTAMP)
                .addValue("updatedAt", Timestamp.valueOf(booking.getUpdatedAt()), Types.TIMESTAMP);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        namedParameterJdbcTemplate.update(sql, params, keyHolder, new String[]{"id"});
        booking.setId(keyHolder.getKey().longValue());
    }

    public void update(BookingEntity booking) {
        String sql = """
                UPDATE bookings 
                SET scheduled_date = :scheduledDate,
                    status = :status,
                    message = :message,
                    updated_at = :updatedAt
                WHERE id = :id
                """;

        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("scheduledDate", Timestamp.valueOf(booking.getScheduledDate()), Types.TIMESTAMP)
                .addValue("status", booking.getStatus().name(), Types.VARCHAR)
                .addValue("message", booking.getMessage(), Types.VARCHAR)
                .addValue("updatedAt", Timestamp.valueOf(booking.getUpdatedAt()), Types.TIMESTAMP)
                .addValue("id", booking.getId(), Types.BIGINT);

        namedParameterJdbcTemplate.update(sql, params);
    }

    public Optional<BookingEntity> findById(Long id) {
        String sql = "SELECT * FROM bookings WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("id", id, Types.BIGINT);
        List<BookingEntity> results = namedParameterJdbcTemplate.query(sql, params, new BookingEntityRowMapper());
        return results.isEmpty() ? Optional.empty() : Optional.of(results.get(0));
    }

    public List<BookingEntity> findByHomeSeekerId(Long homeSeekerId) {
        String sql = "SELECT * FROM bookings WHERE home_seeker_id = :homeSeekerId ORDER BY created_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("homeSeekerId", homeSeekerId, Types.BIGINT);
        return namedParameterJdbcTemplate.query(sql, params, new BookingEntityRowMapper());
    }

    public List<BookingEntity> findByPropertyId(Long propertyId) {
        String sql = "SELECT * FROM bookings WHERE property_id = :propertyId ORDER BY created_at DESC";
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("propertyId", propertyId, Types.BIGINT);
        return namedParameterJdbcTemplate.query(sql, params, new BookingEntityRowMapper());
    }

    public List<BookingEntity> findByProviderId(Long providerId) {
        String sql = """
                SELECT b.* FROM bookings b
                INNER JOIN properties p ON b.property_id = p.id
                WHERE p.provider_id = :providerId
                ORDER BY b.created_at DESC
                """;
        MapSqlParameterSource params = new MapSqlParameterSource().addValue("providerId", providerId, Types.BIGINT);
        return namedParameterJdbcTemplate.query(sql, params, new BookingEntityRowMapper());
    }

    // All booking mutations take this transaction-scoped lock before reading or writing.
    // This also protects empty time slots from simultaneous conflicting requests.
    public void lockSchedule() {
        namedParameterJdbcTemplate.query(
                "SELECT pg_advisory_xact_lock(724019831)",
                new MapSqlParameterSource(), (rs, rowNum) -> 0);
    }

    public boolean hasConflict(Long propertyId, Long providerId, Long seekerId,
                               LocalDateTime scheduledDate, Long excludedBookingId) {
        String sql = """
                SELECT COUNT(*) FROM bookings b
                INNER JOIN properties p ON p.id = b.property_id
                WHERE b.status IN ('PENDING', 'APPROVED')
                  AND b.scheduled_date > :windowStart
                  AND b.scheduled_date < :windowEnd
                  AND (:excludedId IS NULL OR b.id <> :excludedId)
                  AND (b.property_id = :propertyId OR p.provider_id = :providerId
                       OR b.home_seeker_id = :seekerId
                       OR p.provider_id = :seekerId OR b.home_seeker_id = :providerId)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("propertyId", propertyId, Types.BIGINT)
                .addValue("providerId", providerId, Types.BIGINT)
                .addValue("seekerId", seekerId, Types.BIGINT)
                .addValue("excludedId", excludedBookingId, Types.BIGINT)
                .addValue("windowStart", Timestamp.valueOf(scheduledDate.minusHours(1)), Types.TIMESTAMP)
                .addValue("windowEnd", Timestamp.valueOf(scheduledDate.plusHours(1)), Types.TIMESTAMP);
        Long count = namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
        return count != null && count > 0;
    }
}
