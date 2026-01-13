package com.example.HMS.controllers;

import com.example.HMS.dto.PatientDTO;
import com.example.HMS.dto.PatientRequest;
import com.example.HMS.models.Patient;
import com.example.HMS.services.PatientService;
import com.example.HMS.util.EntityDtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Updated Patient Controller with DTOs
 *
 * Key improvements:
 * - Uses DTOs instead of entities
 * - Validation with @Valid
 * - Proper HTTP status codes with ResponseEntity
 * - CORS configured properly
 */
@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:4200") // Configure as needed
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private EntityDtoMapper mapper;

    /**
     * Get all patients
     * Returns: List of PatientDTO (not entities)
     */
    @GetMapping
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();

        // Convert entities to DTOs
        List<PatientDTO> patientDTOs = patients.stream()
                .map(mapper::toPatientDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(patientDTOs);
    }

    /**
     * Create new patient
     *
     * @Valid triggers validation on PatientRequest
     * Returns: 201 Created with PatientDTO
     */
    @PostMapping
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientRequest request) {
        Patient patient = mapper.toPatientEntity(request);
        Patient savedPatient = patientService.createPatient(patient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toPatientDTO(savedPatient));
    }

    /**
     * Get patient by ID
     * Returns: 200 OK with PatientDTO or 404 Not Found (handled by GlobalExceptionHandler)
     */
    @GetMapping("/{id}")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(mapper.toPatientDTO(patient));
    }

    /**
     * Delete patient by ID
     * Returns: 204 No Content (successful deletion with no body)
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update patient by ID
     * Returns: 200 OK with updated PatientDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<PatientDTO> updatePatientById(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        Patient updatedPatient = patientService.updatePatientById(id, mapper.toPatientEntity(request));
        return ResponseEntity.ok(mapper.toPatientDTO(updatedPatient));
    }
}