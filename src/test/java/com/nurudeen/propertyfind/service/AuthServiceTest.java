package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.entity.UserEnum;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.JwtService;
import com.nurudeen.propertyfind.util.PasswordUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private PasswordUtils passwordUtils;

    @InjectMocks
    private AuthService authService;

    private UserEntity mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new UserEntity();
        mockUser.setId(1L);
        mockUser.setEmail("test@example.com");
        mockUser.setFullName("Test User");
        mockUser.setPassword("hashedPassword");
        mockUser.setRole(UserEnum.HOME_SEEKER);
    }

    @Test
    void login_Success() {
        // Arrange
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("plainPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordUtils.matches("plainPassword", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyLong(), anyString())).thenReturn("mockAccessToken");
        when(jwtService.generateRefreshToken(anyString(), anyLong(), anyString())).thenReturn("mockRefreshToken");

        // Act
        LoginResponseDto response = authService.login(dto);

        // Assert
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("mockAccessToken", response.getToken());
        assertEquals("mockRefreshToken", response.getRefreshToken());
    }

    @Test
    void login_UserNotFound_ThrowsBadCredentialsException() {
        // Arrange
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("unknown@example.com");
        dto.setPassword("plainPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(dto));
        verify(passwordUtils, never()).matches(anyString(), anyString());
        verify(jwtService, never()).generateToken(anyString(), anyLong(), anyString());
    }

    @Test
    void login_IncorrectPassword_ThrowsBadCredentialsException() {
        // Arrange
        LoginRequestDto dto = new LoginRequestDto();
        dto.setEmail("test@example.com");
        dto.setPassword("wrongPassword");

        when(userRepository.findByEmail(dto.getEmail())).thenReturn(Optional.of(mockUser));
        when(passwordUtils.matches("wrongPassword", "hashedPassword")).thenReturn(false);

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> authService.login(dto));
        verify(jwtService, never()).generateToken(anyString(), anyLong(), anyString());
    }
}
