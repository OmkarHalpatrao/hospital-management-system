package com.example.HMS.services;

import com.example.HMS.Repository.BillRepository;
import com.example.HMS.Repository.PatientRepository;
import com.example.HMS.exceptions.ResourceNotFoundException;
import com.example.HMS.models.Bill;
import com.example.HMS.models.Patient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BillService {

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private PatientRepository patientRepository;

    public Bill createBill(Long patientId, double amount, String status) {

        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        Bill bill = new Bill();
        bill.setPatient(patient);
        bill.setAmount(amount);
        bill.setStatus(status);

        return billRepository.save(bill);
    }

    public List<Bill> getBillsByPatient(Long patientId) {
        // Verify patient exists
        patientRepository.findById(patientId)
                .orElseThrow(() -> new ResourceNotFoundException("Patient", "id", patientId));

        return billRepository.findByPatientId(patientId);
    }
}