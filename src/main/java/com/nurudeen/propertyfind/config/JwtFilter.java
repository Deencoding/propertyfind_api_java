package com.nurudeen.propertyfind.config;

import com.nurudeen.propertyfind.security.CustomUserPrincipal;
import com.nurudeen.propertyfind.security.JwtService;
import com.nurudeen.propertyfind.service.MyUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MyUserDetailsService userDetailsService;

    public JwtFilter(JwtService jwtService, MyUserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        String token = null;
        Long userId = null;

        // Extract JWT token from the Authorization header
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.substring(7);
            try {
                // Extract user ID (since you store it as the subject)
                userId = jwtService.extractUserId(token);
            } catch (Exception e) {
                logger.warn("Invalid or expired JWT token", e);
            }
        }

        // Proceed only if userId is valid and user not yet authenticated
        if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                // Load the user by ID
                UserDetails userDetails = userDetailsService.loadUserById(userId);


                // Validate token using the token and username(email)
                if (jwtService.isTokenValid(token, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            } catch (Exception e) {
                logger.warn("JWT validation failed", e);
            }
        }

        // Continue with the next filter in the chain
        filterChain.doFilter(request, response);
    }
}
