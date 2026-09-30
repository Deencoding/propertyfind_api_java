package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.booking.BookingCreateDto;
import com.nurudeen.propertyfind.dto.booking.BookingRescheduleDto;
import com.nurudeen.propertyfind.dto.booking.BookingResponseDto;
import com.nurudeen.propertyfind.entity.BookingEntity;
import com.nurudeen.propertyfind.entity.BookingStatus;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.exception.BookingConflictException;
import com.nurudeen.propertyfind.exception.InvalidBookingException;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.BookingMapper;
import com.nurudeen.propertyfind.repository.BookingRepository;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.util.SecurityUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PropertyRepository propertyRepository;
    private final BookingMapper bookingMapper;

    public BookingService(BookingRepository bookingRepository, PropertyRepository propertyRepository,
                          BookingMapper bookingMapper) {
        this.bookingRepository = bookingRepository;
        this.propertyRepository = propertyRepository;
        this.bookingMapper = bookingMapper;
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto createBooking(BookingCreateDto dto) {
        Long seekerId = SecurityUtils.getCurrentUser().getId();
        bookingRepository.lockSchedule();
        requireFuture(dto.getScheduledDate());
        PropertyEntity property = requireProperty(dto.getPropertyId());
        requireAvailable(property);
        if (seekerId.equals(property.getProviderId())) {
            throw new InvalidBookingException("You cannot book a viewing of your own property");
        }
        requireFreeSlot(property, seekerId, dto.getScheduledDate(), null);

        BookingEntity booking = new BookingEntity();
        booking.setPropertyId(property.getId());
        booking.setHomeSeekerId(seekerId);
        booking.setScheduledDate(dto.getScheduledDate());
        booking.setMessage(dto.getMessage());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(booking.getCreatedAt());
        bookingRepository.save(booking);
        return bookingMapper.toResponse(booking);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto updateBookingStatus(Long bookingId, BookingStatus newStatus) {
        Long userId = SecurityUtils.getCurrentUser().getId();
        bookingRepository.lockSchedule();
        BookingEntity booking = requireBooking(bookingId);
        PropertyEntity property = requireProperty(booking.getPropertyId());
        if (newStatus == BookingStatus.CANCELLED) {
            requireParticipant(booking, property, userId);
        } else {
            requireProvider(property, userId);
        }
        requireActive(booking);

        if (newStatus == BookingStatus.APPROVED || newStatus == BookingStatus.REJECTED) {
            if (booking.getStatus() != BookingStatus.PENDING) {
                throw new BookingConflictException("Only pending bookings can be approved or rejected");
            }
            requireFuture(booking.getScheduledDate());
            if (newStatus == BookingStatus.APPROVED) {
                requireAvailable(property);
                requireFreeSlot(property, booking.getHomeSeekerId(), booking.getScheduledDate(), bookingId);
            }
        } else if (newStatus == BookingStatus.COMPLETED) {
            if (booking.getStatus() != BookingStatus.APPROVED
                    || booking.getScheduledDate().plusHours(1).isAfter(LocalDateTime.now())) {
                throw new BookingConflictException("Only approved viewings that have ended can be completed");
            }
        } else if (newStatus != BookingStatus.CANCELLED) {
            throw new BookingConflictException("Invalid booking status transition");
        }

        booking.setStatus(newStatus);
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.update(booking);
        return bookingMapper.toResponse(booking);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public BookingResponseDto rescheduleBooking(Long bookingId, BookingRescheduleDto dto) {
        Long userId = SecurityUtils.getCurrentUser().getId();
        bookingRepository.lockSchedule();
        BookingEntity booking = requireBooking(bookingId);
        PropertyEntity property = requireProperty(booking.getPropertyId());
        requireProvider(property, userId);
        requireActive(booking);
        requireFuture(booking.getScheduledDate());
        requireFuture(dto.getScheduledDate());
        requireAvailable(property);
        requireFreeSlot(property, booking.getHomeSeekerId(), dto.getScheduledDate(), bookingId);
        booking.setScheduledDate(dto.getScheduledDate());
        booking.setUpdatedAt(LocalDateTime.now());
        bookingRepository.update(booking);
        return bookingMapper.toResponse(booking);
    }

    public BookingResponseDto getBooking(Long bookingId) {
        BookingEntity booking = requireBooking(bookingId);
        PropertyEntity property = requireProperty(booking.getPropertyId());
        requireParticipant(booking, property, SecurityUtils.getCurrentUser().getId());
        return bookingMapper.toResponse(booking);
    }

    public List<BookingResponseDto> getMyBookingsAsSeeker() {
        return bookingRepository.findByHomeSeekerId(SecurityUtils.getCurrentUser().getId()).stream()
                .map(bookingMapper::toResponse).toList();
    }

    public List<BookingResponseDto> getMyBookingsAsProvider() {
        return bookingRepository.findByProviderId(SecurityUtils.getCurrentUser().getId()).stream()
                .map(bookingMapper::toResponse).toList();
    }

    private BookingEntity requireBooking(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id " + bookingId));
    }

    private PropertyEntity requireProperty(Long propertyId) {
        return propertyRepository.findById(propertyId)
                .orElseThrow(() -> new ResourceNotFoundException("Property not found with id " + propertyId));
    }

    private void requireProvider(PropertyEntity property, Long userId) {
        if (!userId.equals(property.getProviderId())) {
            throw new AccessDeniedException("Only the property provider can manage this booking");
        }
    }

    private void requireParticipant(BookingEntity booking, PropertyEntity property, Long userId) {
        if (!userId.equals(booking.getHomeSeekerId()) && !userId.equals(property.getProviderId())) {
            throw new AccessDeniedException("You do not have permission to access this booking");
        }
    }

    private void requireActive(BookingEntity booking) {
        if (booking.getStatus() != BookingStatus.PENDING && booking.getStatus() != BookingStatus.APPROVED) {
            throw new BookingConflictException("Rejected, cancelled, or completed bookings cannot be changed");
        }
    }

    private void requireFuture(LocalDateTime scheduledDate) {
        if (scheduledDate == null || !scheduledDate.isAfter(LocalDateTime.now())) {
            throw new InvalidBookingException("Scheduled date must be in the future");
        }
    }

    private void requireAvailable(PropertyEntity property) {
        if (!property.isAvailable()) {
            throw new BookingConflictException("Property is not available for booking");
        }
    }

    private void requireFreeSlot(PropertyEntity property, Long seekerId, LocalDateTime date, Long excludedId) {
        if (bookingRepository.hasConflict(property.getId(), property.getProviderId(), seekerId, date, excludedId)) {
            throw new BookingConflictException("The property, provider, or seeker already has a viewing in this time slot");
        }
    }
}
