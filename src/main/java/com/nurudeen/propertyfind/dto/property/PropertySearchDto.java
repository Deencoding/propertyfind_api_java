package com.nurudeen.propertyfind.dto.property;

import com.nurudeen.propertyfind.entity.PropertyStatus;

import java.math.BigDecimal;

public class PropertySearchDto {

    private PropertyStatus status;

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }

    private String keyword; // Search in title, description, city, state, country
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private Integer minBedroom;
    private Integer minBathroom;
    private Boolean available;
    private String city;
    private String state;
    
    // Pagination params
    private int page = 0;
    private int size = 10;
    private String sortBy = "listed_date"; // default sort
    private String sortDirection = "DESC";

    // Getters and Setters

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public Integer getMinBedroom() {
        return minBedroom;
    }

    public void setMinBedroom(Integer minBedroom) {
        this.minBedroom = minBedroom;
    }

    public Integer getMinBathroom() {
        return minBathroom;
    }

    public void setMinBathroom(Integer minBathroom) {
        this.minBathroom = minBathroom;
    }

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
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

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public String getSortBy() {
        return sortBy;
    }

    public void setSortBy(String sortBy) {
        this.sortBy = sortBy;
    }

    public String getSortDirection() {
        return sortDirection;
    }

    public void setSortDirection(String sortDirection) {
        this.sortDirection = sortDirection;
    }
}
