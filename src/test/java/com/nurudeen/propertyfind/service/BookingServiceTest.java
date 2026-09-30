package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.booking.BookingCreateDto;
import com.nurudeen.propertyfind.dto.booking.BookingResponseDto;
import com.nurudeen.propertyfind.entity.BookingEntity;
import com.nurudeen.propertyfind.entity.BookingStatus;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.PropertyStatus;
import com.nurudeen.propertyfind.dto.booking.BookingRescheduleDto;
import com.nurudeen.propertyfind.exception.BookingConflictException;
import com.nurudeen.propertyfind.exception.InvalidBookingException;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.repository.BookingRepository;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import com.nurudeen.propertyfind.util.SecurityUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.modelmapper.ModelMapper;
import com.nurudeen.propertyfind.mappers.BookingMapper;
import org.mockito.MockedStatic;
import org.mockito.MockitoAnnotations;
import org.springframework.security.access.AccessDeniedException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class BookingServiceTest {

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private PropertyRepository propertyRepository;

    @Spy
    private BookingMapper bookingMapper = new BookingMapper(new ModelMapper());

    @InjectMocks
    private BookingService bookingService;

    private MockedStatic<SecurityUtils> mockedSecurityUtils;
    private UserEntity mockSeeker;
    private PropertyEntity mockProperty;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockSeeker = new UserEntity();
        mockSeeker.setId(2L); // Seeker ID

        mockProperty = new PropertyEntity();
        mockProperty.setId(100L);
        mockProperty.setProviderId(1L); // Provider ID
        mockProperty.setAvailable(true);

        CustomUserPrincipal principal = new CustomUserPrincipal(mockSeeker);
        mockedSecurityUtils = mockStatic(SecurityUtils.class);
        mockedSecurityUtils.when(SecurityUtils::getCurrentUser).thenReturn(principal);
    }

    @AfterEach
    void tearDown() {
        if (mockedSecurityUtils != null) {
            mockedSecurityUtils.close();
        }
    }

    @Test
    void createBooking_Success() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setPropertyId(100L);
        dto.setScheduledDate(LocalDateTime.now().plusDays(2));
        dto.setMessage("I want to see this property.");

        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));

        BookingResponseDto response = bookingService.createBooking(dto);

        assertNotNull(response);
        assertEquals(BookingStatus.PENDING.name(), response.getStatus());
        verify(bookingRepository, times(1)).save(any(BookingEntity.class));
    }

    @Test
    void updateBookingStatus_ApproveAsSeeker_ThrowsAccessDenied() {
        BookingEntity booking = new BookingEntity();
        booking.setId(50L);
        booking.setPropertyId(100L);
        booking.setHomeSeekerId(2L);
        booking.setStatus(BookingStatus.PENDING);

        when(bookingRepository.findById(50L)).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));

        assertThrows(AccessDeniedException.class, () -> bookingService.updateBookingStatus(50L, BookingStatus.APPROVED));
    }

    @Test
    void updateBookingStatus_CancelAsSeeker_Success() {
        BookingEntity booking = new BookingEntity();
        booking.setId(50L);
        booking.setPropertyId(100L);
        booking.setHomeSeekerId(2L); // Same as current user
        booking.setStatus(BookingStatus.PENDING);
        booking.setScheduledDate(LocalDateTime.now().plusDays(1));
        booking.setCreatedAt(LocalDateTime.now());
        booking.setUpdatedAt(LocalDateTime.now());

        when(bookingRepository.findById(50L)).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));

        BookingResponseDto response = bookingService.updateBookingStatus(50L, BookingStatus.CANCELLED);

        assertEquals(BookingStatus.CANCELLED.name(), response.getStatus());
        verify(bookingRepository, times(1)).update(any(BookingEntity.class));
    }

    @Test
    void rentedAndArchivedPropertiesCannotBeBooked() {
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));
        BookingCreateDto dto = new BookingCreateDto();
        dto.setPropertyId(100L);
        dto.setScheduledDate(LocalDateTime.now().plusDays(2));
        for (PropertyStatus status : new PropertyStatus[]{PropertyStatus.RENTED, PropertyStatus.ARCHIVED}) {
            mockProperty.setStatus(status);
            assertThrows(BookingConflictException.class, () -> bookingService.createBooking(dto));
        }
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void pastDatesAndConflictingSlotsCannotBeBooked() {
        BookingCreateDto dto = new BookingCreateDto();
        dto.setPropertyId(100L);
        dto.setScheduledDate(LocalDateTime.now().minusDays(1));
        assertThrows(InvalidBookingException.class, () -> bookingService.createBooking(dto));
        dto.setScheduledDate(LocalDateTime.now().plusDays(2));
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));
        when(bookingRepository.hasConflict(100L, 1L, 2L, dto.getScheduledDate(), null)).thenReturn(true);
        assertThrows(BookingConflictException.class, () -> bookingService.createBooking(dto));
        verify(bookingRepository, never()).save(any());
    }

    @Test
    void providerCanRescheduleActiveBookingButNotCancelledBooking() {
        mockSeeker.setId(1L);
        BookingEntity booking = new BookingEntity();
        booking.setId(50L);
        booking.setPropertyId(100L);
        booking.setHomeSeekerId(2L);
        booking.setStatus(BookingStatus.APPROVED);
        booking.setScheduledDate(LocalDateTime.now().plusDays(2));
        when(bookingRepository.findById(50L)).thenReturn(Optional.of(booking));
        when(propertyRepository.findById(100L)).thenReturn(Optional.of(mockProperty));
        BookingRescheduleDto dto = new BookingRescheduleDto();
        dto.setScheduledDate(LocalDateTime.now().plusDays(3));
        BookingResponseDto response = bookingService.rescheduleBooking(50L, dto);
        assertEquals(dto.getScheduledDate(), response.getScheduledDate());
        assertEquals("APPROVED", response.getStatus());
        verify(bookingRepository).hasConflict(100L, 1L, 2L, dto.getScheduledDate(), 50L);
        booking.setStatus(BookingStatus.CANCELLED);
        assertThrows(BookingConflictException.class, () -> bookingService.rescheduleBooking(50L, dto));
    }
}
