package com.example.HMS.controllers;

import com.example.HMS.dto.BillDTO;
import com.example.HMS.dto.BillRequest;
import com.example.HMS.models.Bill;
import com.example.HMS.services.BillService;
import com.example.HMS.util.EntityDtoMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/bill")
@CrossOrigin(origins = "http://localhost:4200")
public class BillController {

    @Autowired
    private BillService billService;

    @Autowired
    private EntityDtoMapper mapper;

    /**
     * Create new bill
     * Now uses BillRequest DTO with validation
     */
    @PostMapping
    public ResponseEntity<BillDTO> createBill(@Valid @RequestBody BillRequest request) {
        Bill bill = billService.createBill(
                request.getPatientId(),
                request.getAmount(),
                request.getStatus()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toBillDTO(bill));
    }

    /**
     * Get bills by patient ID
     * Returns list of BillDTO (includes patient name)
     */
    @GetMapping("/patient/{patientId}")
    public ResponseEntity<List<BillDTO>> getBillsByPatient(@PathVariable Long patientId) {
        List<Bill> bills = billService.getBillsByPatient(patientId);

        List<BillDTO> billDTOs = bills.stream()
                .map(mapper::toBillDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(billDTOs);
    }
}