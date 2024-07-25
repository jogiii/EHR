package com.electronichealth.service;

import com.electronichealth.dto.PatientDTO;

import java.util.concurrent.CompletableFuture;

public interface IPatientService {
    CompletableFuture<PatientDTO> getPatientRecordAsync(Long patientId);
    CompletableFuture<PatientDTO> createPatientRecordAsync(PatientDTO patientDTO);
    CompletableFuture<PatientDTO> updatePatientAsync(Long patientId, PatientDTO patientDTO);
}

