package com.example.HMS.exceptions;

/**
 * Custom exception for resource not found scenarios (HTTP 404)
 * Used when a requested entity (Patient, Doctor, etc.) doesn't exist in database
 */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String resourceName, String fieldName, Object fieldValue) {
        super(String.format("%s not found with %s: '%s'", resourceName, fieldName, fieldValue));
    }
}