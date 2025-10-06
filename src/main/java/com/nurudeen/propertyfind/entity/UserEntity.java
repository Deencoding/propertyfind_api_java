package com.nurudeen.propertyfind.entity;

import java.time.LocalDateTime;

public class UserEntity {

    private Long id;
    private String fullName;
    private String email;
    private String password;
    private String phoneNumber;
    private LocalDateTime registeredDate;
    private LocalDateTime updatedAt;
    private boolean active;
    private UserEnum role;

    // Constructors
    public UserEntity() {}

    public UserEntity(Long id, String fullName, String email, String password,
                      String phoneNumber, LocalDateTime registeredDate,
                      LocalDateTime updatedAt, boolean active, UserEnum role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.registeredDate = registeredDate;
        this.updatedAt = updatedAt;
        this.active = active;
        this.role = role;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public LocalDateTime getRegisteredDate() { return registeredDate; }
    public void setRegisteredDate(LocalDateTime registeredDate) { this.registeredDate = registeredDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public UserEnum getRole() { return role; }
    public void setRole(UserEnum role) { this.role = role; }
}
