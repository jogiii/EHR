package com.electronichealth.dto;

import com.electronichealth.entity.MedicalRecord;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PatientDTO {
    private Long id;
    private String name;
    private String DOB;
    private List<MedicalRecordDTO> medicalRecords;
}
