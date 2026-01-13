package com.example.HMS.services;

import com.example.HMS.Repository.PatientRepository;
import com.example.HMS.exceptions.ResourceNotFoundException;
import com.example.HMS.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public List<Patient> getAllPatients() {
        return patientRepository.findAll();
    }

    public Patient createPatient(Patient newPatient) {
        return patientRepository.save(newPatient);
    }

    public Patient getPatientById(Long id) {
        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", id));
    }

    public void deletePatientById(Long id) {
        // Check if patient exists before deleting
        Patient patient = getPatientById(id);
        patientRepository.deleteById(id);
    }

    public Patient updatePatientById(Long id, Patient patient) {
        Patient existing = getPatientById(id);

        existing.setName(patient.getName());
        existing.setAge(patient.getAge());
        existing.setGender(patient.getGender()); // FIXED: Now updates gender field

        return patientRepository.save(existing);
    }
}