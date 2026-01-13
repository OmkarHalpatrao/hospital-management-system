package com.example.HMS.controllers;

import com.example.HMS.dto.AppointmentDTO;
import com.example.HMS.dto.AppointmentRequest;
import com.example.HMS.models.Appointment;
import com.example.HMS.services.AppointmentService;
import com.example.HMS.util.EntityDtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/appointment")
@CrossOrigin(origins = "http://localhost:4200")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private EntityDtoMapper mapper;

    /**
     * Create new appointment
     * Now uses AppointmentRequest DTO with validation
     */
    @PostMapping
    public ResponseEntity<AppointmentDTO> createAppointment(@Valid @RequestBody AppointmentRequest request) {
        Appointment appointment = appointmentService.createAppointment(
                request.getPatientId(),
                request.getDoctorId(),
                request.getDate()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toAppointmentDTO(appointment));
    }

    /**
     * Get appointments by patient ID
     * Returns list of AppointmentDTO (includes doctor info)
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<AppointmentDTO>> getPatientAppointments(@PathVariable Long patientId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByPatient(patientId);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(appointmentDTOs);
    }

    /**
     * Get appointments by doctor ID
     * Returns list of AppointmentDTO (includes patient info)
     */
    @GetMapping("/doctor/{doctorId}")
    public ResponseEntity<List<AppointmentDTO>> getDoctorAppointments(@PathVariable Long doctorId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByDoctor(doctorId);

        List<AppointmentDTO> appointmentDTOs = appointments.stream()
                .map(mapper::toAppointmentDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(appointmentDTOs);
    }
}