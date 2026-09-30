package com.nurudeen.propertyfind.mappers;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.PropertyStatus;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class PropertyStatusMappingTest {
    @Test
    void allResponsesIncludeStatusAndConsistentAvailability() {
        PropertyMapper mapper = new PropertyMapper(new ModelMapper());
        PropertyEntity entity = new PropertyEntity();
        for (PropertyStatus status : PropertyStatus.values()) {
            entity.setStatus(status);
            assertEquals(status, mapper.toResponse(entity).getStatus());
            assertEquals(status, mapper.toCreateResponse(entity).getStatus());
            assertEquals(status, mapper.toUpdateResponse(entity).getStatus());
            assertEquals(status == PropertyStatus.AVAILABLE, mapper.toResponse(entity).isAvailable());
        }
    }

    @Test
    void legacyUnavailableUpdatePreservesArchivedStatus() {
        PropertyEntity entity = new PropertyEntity();
        entity.setStatus(PropertyStatus.ARCHIVED);
        entity.setAvailable(false);
        assertEquals(PropertyStatus.ARCHIVED, entity.getStatus());
        assertFalse(entity.isAvailable());
        entity.setAvailable(true);
        assertEquals(PropertyStatus.AVAILABLE, entity.getStatus());
        entity.setAvailable(false);
        assertEquals(PropertyStatus.RENTED, entity.getStatus());
    }
}
