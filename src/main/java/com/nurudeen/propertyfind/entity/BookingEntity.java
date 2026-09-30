package com.nurudeen.propertyfind.entity;

import java.time.LocalDateTime;

public class BookingEntity {
    private Long id;
    private Long propertyId;
    private Long homeSeekerId;
    private LocalDateTime scheduledDate;
    private BookingStatus status;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public Long getHomeSeekerId() { return homeSeekerId; }
    public void setHomeSeekerId(Long homeSeekerId) { this.homeSeekerId = homeSeekerId; }
    
    public LocalDateTime getScheduledDate() { return scheduledDate; }
    public void setScheduledDate(LocalDateTime scheduledDate) { this.scheduledDate = scheduledDate; }
    
    public BookingStatus getStatus() { return status; }
    public void setStatus(BookingStatus status) { this.status = status; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
