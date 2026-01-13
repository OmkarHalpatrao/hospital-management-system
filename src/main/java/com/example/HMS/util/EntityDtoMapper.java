package com.example.HMS.util;

import com.example.HMS.dto.*;
import com.example.HMS.models.*;
import org.springframework.stereotype.Component;

/**
 * Utility class to map between Entities and DTOs
 *
 * Why use a mapper?
 * - Centralized conversion logic
 * - Easy to maintain
 * - Can add complex mapping logic (computed fields, formatting, etc.)
 *
 * In production, consider using MapStruct library for automatic mapping
 */
@Component
public class EntityDtoMapper {

    // ============ Patient Mappings ============

    public PatientDTO toPatientDTO(Patient patient) {
        if (patient == null) return null;

        return new PatientDTO(
                patient.getId(),
                patient.getName(),
                patient.getGender(),
                patient.getAge()
        );
    }

    public Patient toPatientEntity(PatientRequest request) {
        if (request == null) return null;

        Patient patient = new Patient();
        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());

        return patient;
    }

    public void updatePatientEntity(Patient patient, PatientRequest request) {
        if (patient == null || request == null) return;

        patient.setName(request.getName());
        patient.setGender(request.getGender());
        patient.setAge(request.getAge());
    }

    // ============ Doctor Mappings ============

    public DoctorDTO toDoctorDTO(Doctor doctor) {
        if (doctor == null) return null;

        return new DoctorDTO(
                doctor.getId(),
                doctor.getName(),
                doctor.getSpeciality(),
                doctor.getAge()
        );
    }

    public Doctor toDoctorEntity(DoctorRequest request) {
        if (request == null) return null;

        Doctor doctor = new Doctor();
        doctor.setName(request.getName());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setAge(request.getAge());

        return doctor;
    }

    public void updateDoctorEntity(Doctor doctor, DoctorRequest request) {
        if (doctor == null || request == null) return;

        doctor.setName(request.getName());
        doctor.setSpeciality(request.getSpeciality());
        doctor.setAge(request.getAge());
    }

    // ============ Appointment Mappings ============

    public AppointmentDTO toAppointmentDTO(Appointment appointment) {
        if (appointment == null) return null;

        AppointmentDTO dto = new AppointmentDTO();
        dto.setId(appointment.getId());
        dto.setDate(appointment.getDate());

        // Map patient info
        if (appointment.getPatient() != null) {
            dto.setPatientId(appointment.getPatient().getId());
            dto.setPatientName(appointment.getPatient().getName());
        }

        // Map doctor info
        if (appointment.getDoctor() != null) {
            dto.setDoctorId(appointment.getDoctor().getId());
            dto.setDoctorName(appointment.getDoctor().getName());
            dto.setDoctorSpeciality(appointment.getDoctor().getSpeciality());
        }

        return dto;
    }

    // ============ Bill Mappings ============

    public BillDTO toBillDTO(Bill bill) {
        if (bill == null) return null;

        BillDTO dto = new BillDTO();
        dto.setId(bill.getId());
        dto.setAmount(bill.getAmount());
        dto.setStatus(bill.getStatus());

        // Map patient info
        if (bill.getPatient() != null) {
            dto.setPatientId(bill.getPatient().getId());
            dto.setPatientName(bill.getPatient().getName());
        }

        return dto;
    }
}