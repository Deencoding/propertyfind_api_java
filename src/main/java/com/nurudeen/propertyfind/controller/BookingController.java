package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.booking.BookingCreateDto;
import com.nurudeen.propertyfind.dto.booking.BookingRescheduleDto;
import com.nurudeen.propertyfind.dto.booking.BookingResponseDto;
import com.nurudeen.propertyfind.entity.BookingStatus;
import com.nurudeen.propertyfind.service.BookingService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<BookingResponseDto> createBooking(@Valid @RequestBody BookingCreateDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(bookingService.createBooking(dto));
    }

    @PutMapping("/{bookingId}/approve")
    public ResponseEntity<BookingResponseDto> approveBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, BookingStatus.APPROVED));
    }

    @PutMapping("/{bookingId}/reject")
    public ResponseEntity<BookingResponseDto> rejectBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, BookingStatus.REJECTED));
    }

    @PutMapping("/{bookingId}/cancel")
    public ResponseEntity<BookingResponseDto> cancelBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, BookingStatus.CANCELLED));
    }

    @PutMapping("/{bookingId}/reschedule")
    public ResponseEntity<BookingResponseDto> rescheduleBooking(
            @PathVariable Long bookingId, @Valid @RequestBody BookingRescheduleDto dto) {
        return ResponseEntity.ok(bookingService.rescheduleBooking(bookingId, dto));
    }

    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<BookingResponseDto> completeBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.updateBookingStatus(bookingId, BookingStatus.COMPLETED));
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<BookingResponseDto> getBooking(@PathVariable Long bookingId) {
        return ResponseEntity.ok(bookingService.getBooking(bookingId));
    }

    @GetMapping("/seeker")
    public ResponseEntity<List<BookingResponseDto>> getSeekerBookings() {
        return ResponseEntity.ok(bookingService.getMyBookingsAsSeeker());
    }

    @GetMapping("/provider")
    public ResponseEntity<List<BookingResponseDto>> getProviderBookings() {
        return ResponseEntity.ok(bookingService.getMyBookingsAsProvider());
    }
}
