package com.example.HMS.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Security configuration beans
 *
 * This is separate from SecurityConfig to keep concerns separate
 * SecurityConfig = security filters and rules
 * SecurityBeansConfig = reusable security components
 */
@Configuration
public class SecurityBeansConfig {


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}