package com.nurudeen.propertyfind.dto.property;

import com.nurudeen.propertyfind.entity.PropertyStatus;
import jakarta.validation.constraints.NotNull;

public class PropertyStatusUpdateDto {

    @NotNull(message = "Status is required")
    private PropertyStatus status;

    public PropertyStatus getStatus() {
        return status;
    }

    public void setStatus(PropertyStatus status) {
        this.status = status;
    }
}
