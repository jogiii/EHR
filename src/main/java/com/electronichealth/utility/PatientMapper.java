package com.electronichealth.utility;

import com.electronichealth.dto.MedicalRecordDTO;
import com.electronichealth.dto.PatientDTO;
import com.electronichealth.entity.MedicalRecord;
import com.electronichealth.entity.Patient;


import java.util.List;
import java.util.stream.Collectors;

public class PatientMapper {
    public static PatientDTO convertToDTO(Patient patient) {
        if (patient == null) {
            return null;
        }
        List<MedicalRecordDTO> dtoList = convertMedicalRecordsToDTOs(patient.getMedicalRecords());
        return new PatientDTO(patient.getId(), patient.getName(), patient.getDOB(), dtoList);
    }

    private static List<MedicalRecordDTO> convertMedicalRecordsToDTOs(List<MedicalRecord> medicalRecords) {
        if (medicalRecords == null) {
            return null;
        }
        return medicalRecords.stream()
                .map(PatientMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    private static MedicalRecordDTO convertToDTO(MedicalRecord record) {
        if (record == null) {
            return null;
        }
        MedicalRecordDTO dto = new MedicalRecordDTO();
        dto.setDate(record.getDate());
        dto.setDisease(record.getDisease());
        dto.setTreatment(record.getTreatment());
        dto.setTreatmentHistory(record.getTreatmentHistory());
        dto.setMedications(record.getMedications());
        dto.setId(record.getId());
        return dto;
    }
}
