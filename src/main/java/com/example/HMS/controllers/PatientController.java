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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/patient")
@CrossOrigin(origins = "http://localhost:4200")
public class PatientController {

    @Autowired
    private PatientService patientService;

    @Autowired
    private EntityDtoMapper mapper;

    /**
     * Get all patients
     *
     * Access: ADMIN, DOCTOR, STAFF
     *
     * Doctors need to see all patients for appointments
     * Staff need to see all patients for administrative tasks
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF')")
    public ResponseEntity<List<PatientDTO>> getAllPatients() {
        List<Patient> patients = patientService.getAllPatients();

        List<PatientDTO> patientDTOs = patients.stream()
                .map(mapper::toPatientDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(patientDTOs);
    }

    /**
     * Create new patient
     *
     * Access: ADMIN, STAFF
     *
     * Only administrative roles can create patient records
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PatientDTO> createPatient(@Valid @RequestBody PatientRequest request) {
        Patient patient = mapper.toPatientEntity(request);
        Patient savedPatient = patientService.createPatient(patient);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toPatientDTO(savedPatient));
    }

    /**
     * Get patient by ID
     *
     * Access: ADMIN, DOCTOR, STAFF
     *
     * All medical staff can view patient details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF')")
    public ResponseEntity<PatientDTO> getPatientById(@PathVariable Long id) {
        Patient patient = patientService.getPatientById(id);
        return ResponseEntity.ok(mapper.toPatientDTO(patient));
    }

    /**
     * Delete patient by ID
     *
     * Access: ADMIN only
     *
     * Only administrators can delete patient records (sensitive operation)
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePatientById(@PathVariable Long id) {
        patientService.deletePatientById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update patient by ID
     *
     * Access: ADMIN, STAFF
     *
     * Administrative roles can update patient information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<PatientDTO> updatePatientById(
            @PathVariable Long id,
            @Valid @RequestBody PatientRequest request) {

        Patient updatedPatient = patientService.updatePatientById(id, mapper.toPatientEntity(request));
        return ResponseEntity.ok(mapper.toPatientDTO(updatedPatient));
    }
}