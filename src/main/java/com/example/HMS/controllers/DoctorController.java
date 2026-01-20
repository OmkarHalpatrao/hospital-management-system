package com.example.HMS.controllers;

import com.example.HMS.dto.DoctorDTO;
import com.example.HMS.dto.DoctorRequest;
import com.example.HMS.models.Doctor;
import com.example.HMS.services.DoctorService;
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
@RequestMapping("/doctor")
@CrossOrigin(origins = "http://localhost:4200")
public class DoctorController {

    @Autowired
    private DoctorService doctorService;

    @Autowired
    private EntityDtoMapper mapper;

    /**
     * Get all doctors
     *
     * Access: Everyone (patients need to see doctors for booking)
     *
     * Public information for appointment booking
     */
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF', 'PATIENT')")
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();

        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(mapper::toDoctorDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorDTOs);
    }

    /**
     * Create new doctor
     *
     * Access: ADMIN, STAFF
     *
     * Only administrative roles can add new doctors
     */
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorRequest request) {
        Doctor doctor = mapper.toDoctorEntity(request);
        Doctor savedDoctor = doctorService.createDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDoctorDTO(savedDoctor));
    }

    /**
     * Get doctor by ID
     *
     * Access: Everyone
     *
     * Anyone can view doctor details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'DOCTOR', 'STAFF', 'PATIENT')")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(mapper.toDoctorDTO(doctor));
    }

    /**
     * Delete doctor by ID
     *
     * Access: ADMIN only
     *
     * Critical operation - only admins
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Update doctor by ID
     *
     * Access: ADMIN, STAFF
     *
     * Administrative roles can update doctor information
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
    public ResponseEntity<DoctorDTO> updateDoctorById(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {

        Doctor updatedDoctor = doctorService.updateDoctorById(id, mapper.toDoctorEntity(request));
        return ResponseEntity.ok(mapper.toDoctorDTO(updatedDoctor));
    }
}