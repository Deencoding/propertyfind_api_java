package com.nurudeen.propertyfind.entity;

import java.time.LocalDateTime;

public class FavoriteEntity {
    private Long id;
    private Long userId;
    private Long propertyId;
    private LocalDateTime createdAt;

    public FavoriteEntity() {}

    public FavoriteEntity(Long userId, Long propertyId, LocalDateTime createdAt) {
        this.userId = userId;
        this.propertyId = propertyId;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPropertyId() {
        return propertyId;
    }

    public void setPropertyId(Long propertyId) {
        this.propertyId = propertyId;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
