package com.nurudeen.propertyfind.dto.review;

import java.math.BigDecimal;

public class RatingSummaryResponseDto {
    private BigDecimal averageRating;
    private long reviewCount;

    public BigDecimal getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(BigDecimal averageRating) {
        this.averageRating = averageRating;
    }

    public long getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(long reviewCount) {
        this.reviewCount = reviewCount;
    }
}
