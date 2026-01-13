package com.example.HMS.enums;


public enum Role {
    ADMIN,      // Full system access
    DOCTOR,     // Can manage appointments, view patients
    PATIENT,    // Can view own records, book appointments
    STAFF       // Can manage bills, basic operations
}