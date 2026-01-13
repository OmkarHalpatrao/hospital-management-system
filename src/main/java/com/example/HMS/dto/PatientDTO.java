package com.example.HMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {

    private Long id;
    private String name;
    private String gender;
    private int age;

    // We can add computed fields here that don't exist in entity
    // Example: private String fullInfo; // "John Doe (Male, 30)"
}