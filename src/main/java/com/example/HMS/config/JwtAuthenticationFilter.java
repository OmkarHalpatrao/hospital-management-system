package com.example.HMS.config;

import com.example.HMS.services.CustomUserDetailsService;
import com.example.HMS.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * JWT Authentication Filter
 *
 * This filter intercepts EVERY HTTP request before it reaches the controller
 *
 * Flow:
 * 1. Extract JWT token from Authorization header
 * 2. Validate token
 * 3. If valid: Load user and set authentication in SecurityContext
 * 4. If invalid: Do nothing (request will be rejected by Spring Security)
 * 5. Pass request to next filter in chain
 *
 * Why OncePerRequestFilter?
 * - Ensures filter is executed only once per request
 * - Even if request is forwarded/included multiple times
 *
 * Authorization Header Format:
 * "Authorization: Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    /**
     * Main filter method - executed for every request
     *
     * @param request HTTP request
     * @param response HTTP response
     * @param filterChain Chain of filters
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1: Extract Authorization header
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // Step 2: Check if header exists and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No token, continue to next filter
            // Spring Security will handle unauthorized access
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3: Extract token (remove "Bearer " prefix)
        jwt = authHeader.substring(7);

        try {
            // Step 4: Extract username from token
            username = jwtUtils.extractUsername(jwt);

            // Step 5: Check if username exists and user is not already authenticated
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Step 6: Load user details from database
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                // Step 7: Validate token
                if (jwtUtils.validateToken(jwt, userDetails)) {

                    // Step 8: Create authentication token
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,  // credentials (null after authentication)
                                    userDetails.getAuthorities()  // user roles
                            );

                    // Step 9: Set additional details
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    // Step 10: Set authentication in SecurityContext
                    // This tells Spring Security that the user is authenticated
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        } catch (Exception e) {
            // Token is invalid (expired, malformed, etc.)
            // Log error and continue without authentication
            logger.error("Cannot set user authentication: {}", e);
        }

        // Step 11: Continue to next filter
        filterChain.doFilter(request, response);
    }
}