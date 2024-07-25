package com.electronichealth.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MedicalRecordDTO {
    private Long id;
    private String disease;
    private String treatment;
    private String date;
    private String treatmentHistory;
    private String medications;
}
