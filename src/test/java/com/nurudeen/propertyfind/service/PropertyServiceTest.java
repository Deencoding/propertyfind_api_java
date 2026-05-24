package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.property.*;
import com.nurudeen.propertyfind.entity.PropertyEntity;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.entity.UserEnum;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.PropertyMapper;
import com.nurudeen.propertyfind.repository.PropertyRepository;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PropertyServiceTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @InjectMocks
    private PropertyService propertyService;

    private UserEntity mockUser;
    private CustomUserPrincipal mockPrincipal;
    private PropertyEntity mockProperty;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("owner@example.com");
        mockUser.setRole(UserEnum.HOME_SEEKER);

        mockPrincipal = new CustomUserPrincipal(mockUser);

        mockProperty = new PropertyEntity();
        mockProperty.setId(10L);
        mockProperty.setTitle("Beautiful House");
        mockProperty.setProviderId(1L);
        mockProperty.setPricePerYear(BigDecimal.valueOf(15000));
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    private void mockSecurityContext(CustomUserPrincipal principal) {
        SecurityContext securityContext = mock(SecurityContext.class);
        Authentication authentication = mock(Authentication.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.isAuthenticated()).thenReturn(true);
        when(authentication.getPrincipal()).thenReturn(principal);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void createProperty_Success() {
        // Arrange
        mockSecurityContext(mockPrincipal);

        PropertyCreateDto dto = new PropertyCreateDto();
        dto.setTitle("Beautiful House");
        dto.setPricePerYear(BigDecimal.valueOf(15000));

        PropertyCreateResponseDto responseDto = new PropertyCreateResponseDto();
        responseDto.setId(10L);
        responseDto.setTitle("Beautiful House");

        when(propertyMapper.toEntity(dto)).thenReturn(mockProperty);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(propertyMapper.toCreateResponse(any(PropertyEntity.class))).thenReturn(responseDto);

        // Act
        PropertyCreateResponseDto result = propertyService.createProperty(dto);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals("Beautiful House", result.getTitle());
        verify(propertyRepository).save(any(PropertyEntity.class));
    }

    @Test
    void getPropertyById_Success() {
        // Arrange
        PropertyResponseDto responseDto = new PropertyResponseDto();
        responseDto.setId(10L);
        responseDto.setTitle("Beautiful House");

        when(propertyRepository.findById(10L)).thenReturn(Optional.of(mockProperty));
        when(propertyMapper.toResponse(mockProperty)).thenReturn(responseDto);

        // Act
        PropertyResponseDto result = propertyService.getPropertyById(10L);

        // Assert
        assertNotNull(result);
        assertEquals(10L, result.getId());
    }

    @Test
    void getPropertyById_NotFound_ThrowsException() {
        // Arrange
        when(propertyRepository.findById(99L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> propertyService.getPropertyById(99L));
    }

    @Test
    void updateProperty_Success_WhenOwner() {
        // Arrange
        mockSecurityContext(mockPrincipal); // User ID 1

        PropertyUpdateDto updateDto = new PropertyUpdateDto();
        updateDto.setTitle("Updated House");

        PropertyUpdateResponseDto responseDto = new PropertyUpdateResponseDto();
        responseDto.setId(10L);
        responseDto.setTitle("Updated House");

        when(propertyRepository.findById(10L)).thenReturn(Optional.of(mockProperty));
        when(propertyMapper.toUpdateResponse(mockProperty)).thenReturn(responseDto);

        // Act
        PropertyUpdateResponseDto result = propertyService.updateProperty(10L, updateDto);

        // Assert
        assertNotNull(result);
        assertEquals("Updated House", result.getTitle());
        verify(propertyRepository).update(mockProperty);
    }

    @Test
    void updateProperty_AccessDenied_WhenNotOwner() {
        // Arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L); // Different ID
        otherUser.setRole(UserEnum.HOME_SEEKER);
        CustomUserPrincipal otherPrincipal = new CustomUserPrincipal(otherUser);
        mockSecurityContext(otherPrincipal);

        PropertyUpdateDto updateDto = new PropertyUpdateDto();
        updateDto.setTitle("Updated House");

        when(propertyRepository.findById(10L)).thenReturn(Optional.of(mockProperty));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> propertyService.updateProperty(10L, updateDto));
        verify(propertyRepository, never()).update(any(PropertyEntity.class));
    }

    @Test
    void deleteProperty_Success_WhenOwner() {
        // Arrange
        mockSecurityContext(mockPrincipal); // User ID 1
        when(propertyRepository.findById(10L)).thenReturn(Optional.of(mockProperty));

        // Act
        propertyService.deleteProperty(10L);

        // Assert
        verify(propertyRepository).delete(10L);
    }

    @Test
    void deleteProperty_AccessDenied_WhenNotOwner() {
        // Arrange
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L); // Different ID
        otherUser.setRole(UserEnum.HOME_SEEKER);
        CustomUserPrincipal otherPrincipal = new CustomUserPrincipal(otherUser);
        mockSecurityContext(otherPrincipal);

        when(propertyRepository.findById(10L)).thenReturn(Optional.of(mockProperty));

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> propertyService.deleteProperty(10L));
        verify(propertyRepository, never()).delete(anyLong());
    }
}
