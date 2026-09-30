package com.nurudeen.propertyfind.dto.booking;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class BookingCreateDto {
    
    @NotNull(message = "Property ID cannot be null")
    @Positive(message = "Property ID must be positive")
    private Long propertyId;
    
    @NotNull(message = "Scheduled date cannot be null")
    @Future(message = "Scheduled date must be in the future")
    private LocalDateTime scheduledDate;
    
    @Size(max = 1000, message = "Message must not exceed 1000 characters")
    private String message;

    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }

    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
