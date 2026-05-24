package com.nurudeen.propertyfind.util;

import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {

    private SecurityUtils() {
        // Prevent instantiation
    }

    public static CustomUserPrincipal getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("You must be logged in to access this resource");
        }
        return (CustomUserPrincipal) auth.getPrincipal();
    }

    public static void checkAccess(Long ownerId) {
        CustomUserPrincipal principal = getCurrentUser();
        if (!principal.getId().equals(ownerId) && !principal.getRole().name().equals("ADMIN")) {
            throw new AccessDeniedException("You do not have permission to access this resource");
        }
    }
}
