package com.example.HMS.Repository;

import com.example.HMS.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for User entity
 *
 * Spring Security will use findByUsername() to load user during authentication
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by username (for login)
     * Used by Spring Security's UserDetailsService
     */
    Optional<User> findByUsername(String username);

    /**
     * Check if username already exists (for registration validation)
     */
    boolean existsByUsername(String username);

    /**
     * Check if email already exists (for registration validation)
     */
    boolean existsByEmail(String email);
}