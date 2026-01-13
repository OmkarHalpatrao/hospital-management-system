package com.example.HMS.services;

import com.example.HMS.Repository.UserRepository;
import com.example.HMS.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Custom UserDetailsService implementation
 *
 * Spring Security calls loadUserByUsername() during authentication
 * to fetch user details from the database
 *
 * Flow:
 * 1. User sends login request with username/password
 * 2. Spring Security calls this.loadUserByUsername(username)
 * 3. We fetch User entity from database
 * 4. Return User (which implements UserDetails)
 * 5. Spring Security compares passwords
 * 6. If match, authentication successful
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Load user by username
     *
     * Called by Spring Security during authentication
     *
     * @param username The username to search for
     * @return UserDetails object (our User entity implements this)
     * @throws UsernameNotFoundException If user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "User not found with username: " + username
                ));
    }
}