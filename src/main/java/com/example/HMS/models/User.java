package com.example.HMS.models;

import com.example.HMS.enums.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * User entity for authentication and authorization
 *
 * Implements UserDetails interface (required by Spring Security)
 * This allows Spring Security to use our User entity for authentication
 *
 * Key fields:
 * - username: Unique identifier for login
 * - password: BCrypt hashed password (never store plain text!)
 * - email: For contact/verification
 * - role: User's permission level (ADMIN, DOCTOR, PATIENT, STAFF)
 * - enabled: Account active/inactive flag
 */
@Entity
@Table(name = "users") // "user" is a reserved keyword in some databases
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Will store BCrypt hashed password

    @Column(unique = true, nullable = false)
    private String email;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private boolean enabled = true; // Can disable user accounts

    // Optional: Link to Patient or Doctor entity
    // @OneToOne
    // private Patient patient;

    // ========== UserDetails Interface Methods ==========
    // These are required by Spring Security

    /**
     * Returns user's authorities (roles/permissions)
     * Spring Security uses this for authorization checks
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convert role to GrantedAuthority
        // "ROLE_" prefix is Spring Security convention
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    /**
     * Account expiration - return true if account is not expired
     * We're not tracking expiration, so always return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * Account locking - return true if account is not locked
     * We're not tracking locks, so always return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * Credentials expiration - return true if credentials are not expired
     * We're not tracking credential expiration, so always return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * Enabled status - return true if account is enabled
     * This we DO track with the enabled field
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }
}