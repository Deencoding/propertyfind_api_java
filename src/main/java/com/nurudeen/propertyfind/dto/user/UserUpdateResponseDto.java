package com.nurudeen.propertyfind.dto.user;


import java.time.LocalDate;

public class UserUpdateResponseDto {

    private Long id;
    private String fullName;
    private String email;
    private String phoneNumber;
    private LocalDate registeredDate;
    private LocalDate updatedAt;

    // no args constructor, needed for jpa

    public UserUpdateResponseDto(){

    }

    // all args constructor used service layer when mapping from an entity (User) to a DTO


    public UserUpdateResponseDto(Long id, String fullName, String email, String phoneNumber, LocalDate registeredDate, LocalDate updatedAt) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.registeredDate = registeredDate;
        this.updatedAt = updatedAt;
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

    public LocalDate getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDate registeredDate) {
        this.registeredDate = registeredDate;
    }

    public LocalDate getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDate updatedAt) {
        this.updatedAt = updatedAt;
    }

    // to string method expose data as a human-readable string

    @Override
    public String toString() {
        return "UserUpdateResponseDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", registeredDate=" + registeredDate +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
