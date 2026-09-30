package com.nurudeen.propertyfind.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public class BookingRescheduleDto {

    @NotNull(message = "Scheduled date cannot be null")
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;

    public LocalDateTime getScheduledDate() {
        return scheduledDate;
    }

    public void setScheduledDate(LocalDateTime scheduledDate) {
        this.scheduledDate = scheduledDate;
    }
}
