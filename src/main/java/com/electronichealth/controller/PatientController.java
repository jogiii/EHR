package com.electronichealth.controller;

import com.electronichealth.dto.PatientDTO;
import com.electronichealth.service.IPatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("api/v1/patients")
public class PatientController {
    @Autowired
    private IPatientService patientService;

    @GetMapping("/getPatientRecord")
    public CompletableFuture<ResponseEntity<PatientDTO>> getPatientRecord(@RequestParam Long patientId) {
        return patientService.getPatientRecordAsync(patientId)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("/createPatientRecord")
    public CompletableFuture<ResponseEntity<PatientDTO>> createPatientRecord(@RequestBody PatientDTO patientDTO) {
        return patientService.createPatientRecordAsync(patientDTO)
                .thenApply(ResponseEntity::ok);
    }

    @PutMapping("/updatePatientRecord")
    public CompletableFuture<ResponseEntity<PatientDTO>> updatePatientRecord(@RequestParam Long patientId, @RequestBody PatientDTO patientDTO) {
        return patientService.updatePatientAsync(patientId, patientDTO)
                .thenApply(ResponseEntity::ok);
    }
}
