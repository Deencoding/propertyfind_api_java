package com.nurudeen.propertyfind.dto.admin;

public class AnalyticsDashboardResponseDto {
    private long totalUsers;
    private long totalHomeSeekers;
    private long totalProviders;
    
    private long totalProperties;
    private long totalAvailableProperties;
    
    private long totalBookings;
    private long pendingBookings;
    private long approvedBookings;
    private long cancelledBookings;
    
    private long totalReviews;
    private double averagePropertyRating;

    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }

    public long getTotalHomeSeekers() { return totalHomeSeekers; }
    public void setTotalHomeSeekers(long totalHomeSeekers) { this.totalHomeSeekers = totalHomeSeekers; }

    public long getTotalProviders() { return totalProviders; }
    public void setTotalProviders(long totalProviders) { this.totalProviders = totalProviders; }

    public long getTotalProperties() { return totalProperties; }
    public void setTotalProperties(long totalProperties) { this.totalProperties = totalProperties; }

    public long getTotalAvailableProperties() { return totalAvailableProperties; }
    public void setTotalAvailableProperties(long totalAvailableProperties) { this.totalAvailableProperties = totalAvailableProperties; }

    public long getTotalBookings() { return totalBookings; }
    public void setTotalBookings(long totalBookings) { this.totalBookings = totalBookings; }

    public long getPendingBookings() { return pendingBookings; }
    public void setPendingBookings(long pendingBookings) { this.pendingBookings = pendingBookings; }

    public long getApprovedBookings() { return approvedBookings; }
    public void setApprovedBookings(long approvedBookings) { this.approvedBookings = approvedBookings; }

    public long getCancelledBookings() { return cancelledBookings; }
    public void setCancelledBookings(long cancelledBookings) { this.cancelledBookings = cancelledBookings; }

    public long getTotalReviews() { return totalReviews; }
    public void setTotalReviews(long totalReviews) { this.totalReviews = totalReviews; }

    public double getAveragePropertyRating() { return averagePropertyRating; }
    public void setAveragePropertyRating(double averagePropertyRating) { this.averagePropertyRating = averagePropertyRating; }
}
