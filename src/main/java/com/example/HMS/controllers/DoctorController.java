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

    @GetMapping
    public ResponseEntity<List<DoctorDTO>> getAllDoctors() {
        List<Doctor> doctors = doctorService.getAllDoctors();

        List<DoctorDTO> doctorDTOs = doctors.stream()
                .map(mapper::toDoctorDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(doctorDTOs);
    }

    @PostMapping
    public ResponseEntity<DoctorDTO> createDoctor(@Valid @RequestBody DoctorRequest request) {
        Doctor doctor = mapper.toDoctorEntity(request);
        Doctor savedDoctor = doctorService.createDoctor(doctor);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDoctorDTO(savedDoctor));
    }

    @GetMapping("/{id}")
    public ResponseEntity<DoctorDTO> getDoctorById(@PathVariable Long id) {
        Doctor doctor = doctorService.getDoctorById(id);
        return ResponseEntity.ok(mapper.toDoctorDTO(doctor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDoctorById(@PathVariable Long id) {
        doctorService.deleteDoctorById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<DoctorDTO> updateDoctorById(
            @PathVariable Long id,
            @Valid @RequestBody DoctorRequest request) {

        Doctor updatedDoctor = doctorService.updateDoctorById(id, mapper.toDoctorEntity(request));
        return ResponseEntity.ok(mapper.toDoctorDTO(updatedDoctor));
    }
}