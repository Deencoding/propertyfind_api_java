package com.nurudeen.propertyfind.controller;

import com.nurudeen.propertyfind.dto.admin.AnalyticsDashboardResponseDto;
import com.nurudeen.propertyfind.service.AdminAnalyticsService;
import com.nurudeen.propertyfind.dto.user.UserResponseDto;
import com.nurudeen.propertyfind.service.UserService;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/admin")
public class AdminController {

   private final UserService userService;
   private final AdminAnalyticsService adminAnalyticsService;

    public AdminController(UserService userService, AdminAnalyticsService adminAnalyticsService) {
        this.userService = userService;
        this.adminAnalyticsService = adminAnalyticsService;
    }

    // get all users
    @GetMapping("/all")
    public ResponseEntity<List<UserResponseDto>> getAllUsers(){
        List<UserResponseDto> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    // get dashboard analytics
    @GetMapping("/dashboard")
    public ResponseEntity<AnalyticsDashboardResponseDto> getDashboard() {
        return ResponseEntity.ok(adminAnalyticsService.getDashboardAnalytics());
    }
}
