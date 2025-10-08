package com.nurudeen.propertyfind.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class PropertyEntity {

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
    private LocalDateTime listedDate;
    private LocalDateTime updatedAt;

    private Long providerId;  // Foreign key reference to providers table

    // Constructors
    public PropertyEntity() {}

    public PropertyEntity(Long id, String description, String title, String address,
                          String city, String state, String country, BigDecimal pricePerYear,
                          int bedroom, int bathroom, double area, List<String> imageUrls,
                          boolean available, LocalDateTime listedDate, LocalDateTime updatedAt,
                          Long providerId) {
        this.id = id;
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
        this.available = available;
        this.listedDate = listedDate;
        this.updatedAt = updatedAt;
        this.providerId = providerId;
    }

    // --- Getters and Setters ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public BigDecimal getPricePerYear() { return pricePerYear; }
    public void setPricePerYear(BigDecimal pricePerYear) { this.pricePerYear = pricePerYear; }

    public int getBedroom() { return bedroom; }
    public void setBedroom(int bedroom) { this.bedroom = bedroom; }

    public int getBathroom() { return bathroom; }
    public void setBathroom(int bathroom) { this.bathroom = bathroom; }

    public double getArea() { return area; }
    public void setArea(double area) { this.area = area; }

    public List<String> getImageUrls() { return imageUrls; }
    public void setImageUrls(List<String> imageUrls) { this.imageUrls = imageUrls; }

    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }

    public LocalDateTime getListedDate() { return listedDate; }
    public void setListedDate(LocalDateTime listedDate) { this.listedDate = listedDate; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    public Long getProviderId() { return providerId; }
    public void setProviderId(Long providerId) { this.providerId = providerId; }
}
