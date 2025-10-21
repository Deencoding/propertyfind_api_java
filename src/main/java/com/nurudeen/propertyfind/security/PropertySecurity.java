package com.nurudeen.propertyfind.security;

import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("propertySecurity")
public class PropertySecurity {

    private final PropertyRepository propertyRepository;

    public PropertySecurity(PropertyRepository propertyRepository) {
        this.propertyRepository = propertyRepository;
    }

    public boolean isOwnerOrAdmin(Long propertyId, Long userId) {
        PropertyEntity property = propertyRepository.findById(propertyId).orElse(null);
        if (property == null) return false;

        Long ownerId = property.getProviderId();
        return ownerId.equals(userId) || hasRole("ADMIN");
    }

    private boolean hasRole(String role) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_" + role));
    }
}
