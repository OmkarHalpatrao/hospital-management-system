package com.example.HMS.controllers;

import com.example.HMS.dto.AuthResponse;
import com.example.HMS.dto.LoginRequest;
import com.example.HMS.dto.RegisterRequest;
import com.example.HMS.models.User;
import com.example.HMS.services.AuthService;
import com.example.HMS.util.JwtUtils;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication Controller
 *
 * Handles:
 * - User registration
 * - User login (returns JWT token)
 *
 * Public endpoints (no authentication required):
 * - POST /auth/register
 * - POST /auth/login
 */
@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private AuthenticationManager authenticationManager;


    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {

        // Step 1: Register user (password will be hashed)
        User user = authService.registerUser(request);

        // Step 2: Generate JWT token for the new user
        String token = jwtUtils.generateToken(user);

        // Step 3: Create response with token and user info
        AuthResponse response = new AuthResponse(
                token,
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );

        // Step 4: Return response with 201 Created status
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {

        try {
            // Step 1: Authenticate user with Spring Security
            // This checks username and password against database
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            // Step 2: If authentication successful, get user details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Step 3: Get full user info from database
            User user = authService.getUserByUsername(userDetails.getUsername());

            // Step 4: Generate JWT token
            String token = jwtUtils.generateToken(userDetails);

            // Step 5: Create response
            AuthResponse response = new AuthResponse(
                    token,
                    user.getUsername(),
                    user.getEmail(),
                    user.getRole()
            );

            // Step 6: Return response
            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            // Authentication failed - wrong username or password
            throw new com.example.HMS.exceptions.UnauthorizedException(
                    "Invalid username or password"
            );
        }
    }

    /**
     * Test endpoint to verify authentication
     *
     * GET /auth/test
     * Requires: Valid JWT token in Authorization header
     *
     * This endpoint is protected and can be used to test if JWT is working
     */
    @GetMapping("/test")
    public ResponseEntity<String> testAuth() {
        return ResponseEntity.ok("Authentication successful! JWT is working.");
    }
}