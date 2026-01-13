package com.example.HMS.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating/updating Doctor
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DoctorRequest {

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 100, message = "Name must be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Speciality is required")
    @Size(min = 2, max = 50, message = "Speciality must be between 2 and 50 characters")
    private String speciality;

    @Min(value = 25, message = "Age must be at least 25")
    @Max(value = 80, message = "Age must be less than 80")
    private int age;
}