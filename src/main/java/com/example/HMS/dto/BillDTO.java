package com.example.HMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillDTO {

    private Long id;
    private double amount;
    private String status;

    // Only essential patient info
    private Long patientId;
    private String patientName;
}