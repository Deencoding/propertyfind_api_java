package com.nurudeen.propertyfind.dto.auth;

import com.nurudeen.propertyfind.entity.UserEnum;

import java.time.LocalDateTime;

public class LoginResponseDto {
    private Long id;
    private String fullName;
    private String email;
    private UserEnum role;
    private LocalDateTime registeredDate;
    private String token;


    public LoginResponseDto(){

    }

    public LoginResponseDto(Long id, String fullName, String email, UserEnum role, LocalDateTime registeredDate) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
        this.registeredDate = registeredDate;
    }

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

    public UserEnum getRole() {
        return role;
    }

    public void setRole(UserEnum role) {
        this.role = role;
    }


    public LocalDateTime getRegisteredDate() {
        return registeredDate;
    }

    public void setRegisteredDate(LocalDateTime registeredDate) {
        this.registeredDate = registeredDate;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    @Override
    public String toString() {
        return "LoginResponseDto{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", role='" + role + '\'' +
                ", registeredDate=" + registeredDate +
                '}';
    }
}
