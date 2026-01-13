package com.example.HMS.services;

import com.example.HMS.Repository.DoctorRepository;
import com.example.HMS.exceptions.ResourceNotFoundException;
import com.example.HMS.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;

    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    public Doctor createDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    public Doctor getDoctorById(Long id) {
        return doctorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Doctor", "id", id));
    }

    public void deleteDoctorById(Long id) {
        // Check if doctor exists before deleting
        Doctor doctor = getDoctorById(id);
        doctorRepository.deleteById(id);
    }

    public Doctor updateDoctorById(Long id, Doctor doctor) {
        Doctor existing = getDoctorById(id);

        existing.setName(doctor.getName());
        existing.setAge(doctor.getAge());
        existing.setSpeciality(doctor.getSpeciality());

        return doctorRepository.save(existing);
    }
}