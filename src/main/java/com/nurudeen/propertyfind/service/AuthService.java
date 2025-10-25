package com.nurudeen.propertyfind.service;

import com.nurudeen.propertyfind.dto.auth.LoginRequestDto;
import com.nurudeen.propertyfind.dto.auth.LoginResponseDto;
import com.nurudeen.propertyfind.entity.UserEntity;
import com.nurudeen.propertyfind.entity.UserEnum;
import com.nurudeen.propertyfind.repository.UserRepository;
import com.nurudeen.propertyfind.security.JwtService;
import com.nurudeen.propertyfind.util.PasswordUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final RestTemplate restTemplate;
    private final PasswordUtils passwordUtils;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String clientId;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String clientSecret;

    public AuthService(AuthenticationManager authenticationManager, UserRepository userRepository, JwtService jwtService, RestTemplate restTemplate, PasswordUtils passwordUtils) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.restTemplate = restTemplate;
        this.passwordUtils = passwordUtils;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId(), user.getRole().name());


        LoginResponseDto response = new LoginResponseDto();
        response.setId(user.getId());
        response.setEmail(user.getEmail());
        response.setFullName(user.getFullName());
        response.setRole(user.getRole());
        response.setRegisteredDate(user.getRegisteredDate());
        response.setToken(token);
        response.setRefreshToken(refreshToken);

        return response;
    }


    public Map<String, String> handleGoogleLogin(String code) {
        // 1. Exchange code for access token
        MultiValueMap<String, String> tokenRequest = new LinkedMultiValueMap<>();
        tokenRequest.add("code", code);
        tokenRequest.add("client_id", clientId);
        tokenRequest.add("client_secret", clientSecret);
        tokenRequest.add("redirect_uri", """
                http://localhost:8080/api/auth/google/callback""");
        tokenRequest.add("grant_type", "authorization_code");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(tokenRequest, headers);

        ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                "https://oauth2.googleapis.com/token",
                requestEntity,
                Map.class
        );


        if (tokenResponse.getBody() == null || !tokenResponse.getBody().containsKey("access_token")) {
            throw new RuntimeException("Failed to get access token from Google");
        }

        String googleAccessToken = (String) tokenResponse.getBody().get("access_token");

        // 2. Use Google access token to get user info
        headers.setBearerAuth(googleAccessToken);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> userInfoResponse = restTemplate.exchange(
                "https://www.googleapis.com/oauth2/v3/userinfo",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map<String, Object> userInfo = userInfoResponse.getBody();
        if (userInfo == null) {
            throw new RuntimeException("Failed to fetch user info from Google");
        }

        String email = (String) userInfo.get("email");
        String name = (String) userInfo.get("name");

        // 3. Check if user exists, else create one
        UserEntity user;
        Optional<UserEntity> existingUser = userRepository.findByEmail(email);
        if (existingUser.isPresent()) {
            user = existingUser.get();
        } else {
            UserEntity newUser = new UserEntity();
            newUser.setEmail(email);
            newUser.setFullName(name);
            newUser.setPassword(passwordUtils.hashPassword(UUID.randomUUID().toString()));
            newUser.setRole(UserEnum.HOME_SEEKER);
            newUser.setRegisteredDate(LocalDateTime.now());
            newUser.setUpdatedAt(LocalDateTime.now());
            userRepository.save(newUser);
            user = newUser;
        }

        // 4. Generate app tokens using your method
        String accessToken = jwtService.generateToken(user.getEmail(), user.getId(), user.getRole().name());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail(), user.getId(), user.getRole().name());

        // 5. Return both tokens
        Map<String, String> response = new HashMap<>();
        response.put("accessToken", accessToken);
        response.put("refreshToken", refreshToken);
        response.put("email", user.getEmail());
        response.put("fullName", user.getFullName());

        return response;
    }
}
