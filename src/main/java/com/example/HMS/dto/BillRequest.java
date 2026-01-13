package com.example.HMS.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for creating Bill
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BillRequest {

    @NotNull(message = "Patient ID is required")
    private Long patientId;

    @Min(value = 0, message = "Amount must be positive")
    private double amount;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "^(Paid|Unpaid|Pending)$", message = "Status must be Paid, Unpaid, or Pending")
    private String status;
}