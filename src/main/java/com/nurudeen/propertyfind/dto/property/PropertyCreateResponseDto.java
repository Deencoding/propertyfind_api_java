package com.nurudeen.propertyfind.dto.property;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PropertyCreateResponseDto {

    private Long id;
    private String description;
    private String title;
    private String address;
    private String city;
    private String state;
    private String country;
    private BigDecimal pricePerYear;
    private int bedroom;
    private int bathroom;
    private double area;
    private List<String> imageUrls;
    private boolean available;
    private boolean active;
    private LocalDateTime listedDate;
    private LocalDateTime updatedAt;

    // Provider details
    private String providerFullName;
    private String providerPhoneNumber;

    // no args constructor needed for jpa

    public PropertyCreateResponseDto(){

    }

    // The all-args constructor is for developer convenience
    // letting you create fully initialized objects in one go

    public PropertyCreateResponseDto(String providerPhoneNumber, String providerFullName, LocalDateTime updatedAt,
                                     LocalDateTime listedDate, boolean active, boolean available,
                                     List<String> imageUrls, double area, int bathroom, int bedroom,
                                     BigDecimal pricePerYear, String country, String state, String city,
                                     String address, String title, String description, Long id) {
        this.providerPhoneNumber = providerPhoneNumber;
        this.providerFullName = providerFullName;
        this.updatedAt = updatedAt;
        this.listedDate = listedDate;
        this.active = active;
        this.available = available;
        this.imageUrls = imageUrls;
        this.area = area;
        this.bathroom = bathroom;
        this.bedroom = bedroom;
        this.pricePerYear = pricePerYear;
        this.country = country;
        this.state = state;
        this.city = city;
        this.address = address;
        this.title = title;
        this.description = description;
        this.id = id;
    }


    // Setters → For frameworks (like Jackson) to map incoming JSON → DTO object.
    // Getters → For code (controller/service/repository) to read those values after the DTO is created.

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getListedDate() {
        return listedDate;
    }

    public void setListedDate(LocalDateTime listedDate) {
        this.listedDate = listedDate;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getProviderFullName() {
        return providerFullName;
    }

    public void setProviderFullName(String providerFullName) {
        this.providerFullName = providerFullName;
    }

    public String getProviderPhoneNumber() {
        return providerPhoneNumber;
    }

    public void setProviderPhoneNumber(String providerPhoneNumber) {
        this.providerPhoneNumber = providerPhoneNumber;
    }

    // to string method expose data as a human-readable string

    @Override
    public String toString() {
        return "PropertyCreateResponseDto{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", title='" + title + '\'' +
                ", address='" + address + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", pricePerYear=" + pricePerYear +
                ", bedroom=" + bedroom +
                ", bathroom=" + bathroom +
                ", area=" + area +
                ", imageUrls=" + imageUrls +
                ", available=" + available +
                ", active=" + active +
                ", listedDate=" + listedDate +
                ", updatedAt=" + updatedAt +
                ", providerFullName='" + providerFullName + '\'' +
                ", providerPhoneNumber='" + providerPhoneNumber + '\'' +
                '}';
    }
}
