package com.nurudeen.propertyfind.dto.user;


import com.nurudeen.propertyfind.entity.UserEnum;

import java.time.LocalDateTime;

public class UserResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDateTime registeredDate;
    private LocalDateTime updatedAt;
    private UserEnum role;


    // no args constructor, needed for jpa

    public  UserResponseDto() {

    }

    // all args constructor used service layer when mapping from an entity (User) to a DTO

    public UserResponseDto(Long id, String fullName, String email, String phoneNumber,
                           LocalDateTime registeredDate, LocalDateTime updatedAt, UserEnum role) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registeredDate = registeredDate;
        this.updatedAt = updatedAt;
        this.role = role;

    }

    //  getters (because object → JSON requires reading values)
    // Setters are optional, but usually included for flexibility.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public UserEnum getRole() {
        return role;
    }

    public void setRole(UserEnum role) {
        this.role = role;
    }

    // to string method expose data as a human-readable string

    @Override
    public String toString() {
        return "UserResponseDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registeredDate=" + registeredDate +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
