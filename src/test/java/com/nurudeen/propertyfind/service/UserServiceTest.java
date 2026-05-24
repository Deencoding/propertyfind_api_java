package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.user.*;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.entity.UserEnum;
import com.nurudeen.propertyfind.exception.DuplicateResourceException;
import com.nurudeen.propertyfind.exception.ResourceNotFoundException;
import com.nurudeen.propertyfind.mappers.UserMapper;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import com.nurudeen.propertyfind.util.PasswordUtils;
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

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordUtils passwordUtils;

    @InjectMocks
    private UserService userService;

    private UserEntity mockUser;
    private CustomUserPrincipal mockPrincipal;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFullName("Test User");
        mockUser.setRole(UserEnum.HOME_SEEKER);
        mockUser.setPassword("hashedPassword");
        
        mockPrincipal = new CustomUserPrincipal(mockUser);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext(); // Clean up context after each test
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
    void createUser_Success() {
        // Arrange
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("test@example.com");
        dto.setPassword("plainPassword");
        dto.setFullName("Test User");

        UserCreateResponseDto responseDto = new UserCreateResponseDto();
        responseDto.setId(1L);
        responseDto.setEmail("test@example.com");

        // We need a specific mock entity for creation that has the plain password initially
        UserEntity mockCreateUser = new UserEntity();
        mockCreateUser.setId(1L);
        mockCreateUser.setEmail("test@example.com");
        mockCreateUser.setFullName("Test User");
        mockCreateUser.setPassword("plainPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());
        when(userMapper.toEntity(dto)).thenReturn(mockCreateUser);
        when(passwordUtils.hashPassword("plainPassword")).thenReturn("hashedPassword");
        when(userMapper.toCreateResponse(any(UserEntity.class))).thenReturn(responseDto);

        // Act
        UserCreateResponseDto result = userService.createUser(dto);

        // Assert
        assertNotNull(result);
        assertEquals("test@example.com", result.getEmail());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void createUser_DuplicateEmail_ThrowsException() {
        // Arrange
        UserCreateDto dto = new UserCreateDto();
        dto.setEmail("test@example.com");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(mockUser));

        // Act & Assert
        assertThrows(DuplicateResourceException.class, () -> userService.createUser(dto));
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void getUserById_Success_WhenUserIsOwner() {
        // Arrange
        mockSecurityContext(mockPrincipal);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toResponse(mockUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void getUserById_AccessDenied_WhenUserIsNotOwnerAndNotAdmin() {
        // Arrange - Logged in as User ID 2
        UserEntity otherUser = new UserEntity();
        otherUser.setId(2L);
        otherUser.setRole(UserEnum.HOME_SEEKER);
        CustomUserPrincipal otherPrincipal = new CustomUserPrincipal(otherUser);
        mockSecurityContext(otherPrincipal);

        // Act & Assert
        assertThrows(AccessDeniedException.class, () -> userService.getUserById(1L));
        verify(userRepository, never()).findById(anyLong()); // Should fail before hitting DB
    }
    
    @Test
    void getUserById_Success_WhenUserIsAdmin() {
        // Arrange - Logged in as User ID 2, but has ADMIN role
        UserEntity adminUser = new UserEntity();
        adminUser.setId(2L);
        adminUser.setRole(UserEnum.ADMIN);
        CustomUserPrincipal adminPrincipal = new CustomUserPrincipal(adminUser);
        mockSecurityContext(adminPrincipal);

        UserResponseDto responseDto = new UserResponseDto();
        responseDto.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userMapper.toResponse(mockUser)).thenReturn(responseDto);

        // Act
        UserResponseDto result = userService.getUserById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }
}
