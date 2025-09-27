package com.nurudeen.propertyfind.dto.property;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.List;

public class PropertyCreateDto {

    @NotBlank(message = "Description is required")
    @Size(min = 2, max = 150, message = "Description must be between 2 and 150 characters")
    private String description;

    @NotBlank(message = "Title is required")
    @Size(min = 2, max = 50, message = "Title must be between 2 and 50 characters")
    private String title;

    @NotBlank(message = "Address is required")
    @Size(min = 2, max = 50, message = "Address must be between 2 and 50 characters")
    private String address;

    @NotBlank(message = "City is required")
    @Size(min = 2, max = 50, message = "City must be between 2 and 50 characters")
    private String city;

    @NotBlank(message = "State is required")
    @Size(min = 2, max = 50, message = "State must be between 2 and 50 characters")
    private String state;

    @NotBlank(message = "Country is required")
    @Size(min = 2, max = 50, message = "Country must be between 2 and 50 characters")
    private String country;

    @NotNull(message = "Price per year is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal pricePerYear;

    @NotBlank(message = "Bedroom is required")
    @Size(min = 2, max = 50, message = "Bedrooms must be between 2 and 50 characters")
    private int bedroom;

    @Min(value = 0, message = "Bathrooms cannot be negative")
    private int bathroom;

    @Positive(message = "Area must be greater than 0")
    private double area;

    @NotEmpty(message = "At least one image URL is required")
    private List<@NotBlank(message = "Image URL cannot be blank") String> imageUrls;


    // no args constructor needed for jpa

    public PropertyCreateDto(){

    }


    // The all-args constructor is for developer convenience
    // letting you create fully initialized objects in one go


    public PropertyCreateDto(String description, String title, String address, String city, String state,
                             String country, BigDecimal pricePerYear, int bedroom, int bathroom, double area,
                             List<String> imageUrls) {
        this.description = description;
        this.title = title;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.pricePerYear = pricePerYear;
        this.bedroom = bedroom;
        this.bathroom = bathroom;
        this.area = area;
        this.imageUrls = imageUrls;

    }


    // Setters → For frameworks (like Jackson) to map incoming JSON → DTO object.
    // Getters → For code (controller/service/repository) to read those values after the DTO is created.

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public BigDecimal getPricePerYear() {
        return pricePerYear;
    }

    public void setPricePerYear(BigDecimal pricePerYear) {
        this.pricePerYear = pricePerYear;
    }

    public int getBedroom() {
        return bedroom;
    }

    public void setBedroom(int bedroom) {
        this.bedroom = bedroom;
    }

    public int getBathroom() {
        return bathroom;
    }

    public void setBathroom(int bathroom) {
        this.bathroom = bathroom;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }


    // to string method expose data as a human-readable string

    @Override
    public String toString() {
        return "PropertyCreateDto{" +
                "description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pricePerYear='" + pricePerYear + '\'' +
                ", bedroom='" + bedroom + '\'' +
                ", bathroom='" + bathroom + '\'' +
                ", area='" + area + '\'' +
                ", imageUrls=" + imageUrls +
                '}';
    }
}
