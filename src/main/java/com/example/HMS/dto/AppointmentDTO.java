package com.example.HMS.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentDTO {

    private Long id;
    private String date;

    // Nested DTOs for related entities (avoid full entity exposure)
    private Long patientId;
    private String patientName;

    private Long doctorId;
    private String doctorName;
    private String doctorSpeciality;
}