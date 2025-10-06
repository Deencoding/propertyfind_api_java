package com.nurudeen.propertyfind.dto.user;


import com.nurudeen.propertyfind.entity.UserEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserCreateDto {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, max = 50, message = "Full name must be between 2 and 50 characters")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Size(message = "Email must be valid")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Phone Number is required")
    @Size(min = 10, max = 15, message = "Phone Number must be between 10 and 15")
    private String phoneNumber;

    private UserEnum role;

    // no args constructor needed for jpa
    public UserCreateDto() {

    }

    // The all-args constructor is for developer convenience
    // letting you create fully initialized objects in one go

    public UserCreateDto(String fullName, String email, String password, String phoneNumber, UserEnum role) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.role = role;
    }


    // Setters → For frameworks (like Jackson) to map incoming JSON → DTO object.
    // Getters → For code (controller/service/repository) to read those values after the DTO is created.

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserEnum getRole() {
        return role;
    }

    // The toString() method used to give a human-readable string representation of the object.
    // can also be used in unit testing

    @Override
    public String toString() {
        return "UserCreateDto{" +
                "fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", role=" + role +
                '}';
    }
}
