package com.example.HMS.services;

import com.example.HMS.Repository.UserRepository;
import com.example.HMS.dto.RegisterRequest;
import com.example.HMS.exceptions.BadRequestException;
import com.example.HMS.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Transactional
    public User registerUser(RegisterRequest request) {
        // Validate username uniqueness
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException(
                    "Username '" + request.getUsername() + "' is already taken"
            );
        }

        // Validate email uniqueness
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException(
                    "Email '" + request.getEmail() + "' is already registered"
            );
        }

        // Create new user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());

        // CRITICAL: Hash password before saving
        // NEVER store plain text passwords!
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        user.setEnabled(true);

        // Save to database
        return userRepository.save(user);
    }


    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new BadRequestException(
                        "User not found with username: " + username
                ));
    }
}