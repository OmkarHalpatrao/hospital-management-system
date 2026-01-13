package com.example.HMS.dto;

import com.example.HMS.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for authentication responses (login/register)
 *
 * Returned after successful login or registration
 * Contains JWT token and user info
 *
 * Example JSON response:
 * {
 *   "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
 *   "type": "Bearer",
 *   "username": "john_doe",
 *   "email": "john@example.com",
 *   "role": "PATIENT"
 * }
 *
 * Frontend will:
 * 1. Store token (usually in localStorage or sessionStorage)
 * 2. Include in Authorization header: "Bearer <token>"
 * 3. Use user info for UI personalization
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {

    private String token;           // JWT token

    private String type = "Bearer"; // Token type (standard for JWT)

    private String username;

    private String email;

    private Role role;

    /**
     * Convenience constructor without type (defaults to "Bearer")
     */
    public AuthResponse(String token, String username, String email, Role role) {
        this.token = token;
        this.type = "Bearer";
        this.username = username;
        this.email = email;
        this.role = role;
    }
}