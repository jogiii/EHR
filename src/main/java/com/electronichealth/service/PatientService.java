package com.electronichealth.service;

import com.electronichealth.dto.MedicalRecordDTO;
import com.electronichealth.dto.PatientDTO;
import com.electronichealth.entity.MedicalRecord;
import com.electronichealth.entity.Patient;
import com.electronichealth.errorhandling.DataNotFoundException;
import com.electronichealth.kafka.Producer;
import com.electronichealth.repository.MedicalRecordRepository;
import com.electronichealth.repository.PatientRepository;
import com.electronichealth.utility.PatientMapper;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;

import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;


@Service
public class PatientService implements IPatientService{
    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private MedicalRecordRepository medicalRecordRepository;

    @Autowired
    private Producer producer;

    private static final Logger logger = LoggerFactory.getLogger(PatientService.class);

    @Override
    @Cacheable(value = "patients", key = "#patientId", cacheManager = "cacheManager")
    public CompletableFuture<PatientDTO> getPatientRecordAsync(Long patientId) {
        return CompletableFuture.supplyAsync(() -> {
            logger.debug("Getting patient details with ID: {}",patientId);
            return patientRepository.findByIdWithMedicalRecords(patientId).map(PatientMapper::convertToDTO).orElseThrow(() -> new DataNotFoundException("Record not found"));
        }, Executors.newCachedThreadPool());
    }

    @Transactional
    public CompletableFuture<PatientDTO> createPatientRecordAsync(PatientDTO record) {
        return CompletableFuture.supplyAsync(() -> {
        logger.info("Saving new patient: {}",record);
        Patient patient = new Patient(record.getName(), record.getDOB());
         List<MedicalRecordDTO> list = record.getMedicalRecords();
         if(!CollectionUtils.isEmpty(list))
         {
             for (MedicalRecordDTO dto:list) {
                 patient.addMedicalRecord(new MedicalRecord(dto.getDisease(),dto.getTreatment(),dto.getDate(),dto.getTreatmentHistory(),dto.getMedications()));
             }
         }

        Patient patient1 = patientRepository.save(patient);
        logger.info("new patient details saved successfully: {}",patient1);
        return PatientMapper.convertToDTO(patient1);
        }, Executors.newCachedThreadPool());
    }


    @Transactional
    public CompletableFuture<PatientDTO> updatePatientAsync(Long patientId, PatientDTO patientDTO) {
        return CompletableFuture.supplyAsync(() -> {
        logger.debug("Updating patient details with ID: {}",patientId);
        Patient patient = patientRepository.findById(patientId)
                .orElseThrow(() -> new RuntimeException("Patient not found"));

        patient.setName(patientDTO.getName());
        patient.setDOB(patientDTO.getDOB());
        updateMedicalRecords(patient, patientDTO.getMedicalRecords());

        Patient updatedPatient = patientRepository.save(patient);
        logger.info("Patient details updated successfully : {}", updatedPatient);
        producer.sendToKafka(patient);
        return PatientMapper.convertToDTO(updatedPatient);
        }, Executors.newCachedThreadPool());
    }

    private void updateMedicalRecords(Patient patient, List<MedicalRecordDTO> medicalRecordDTOs) {
        logger.debug("Updating patient Medical Records");
        if(!CollectionUtils.isEmpty(medicalRecordDTOs)){
        for (MedicalRecordDTO dto : medicalRecordDTOs) {
            MedicalRecord record = patient.getMedicalRecords().stream()
                    .filter(r -> r.getId().equals(dto.getId()))
                    .findFirst()
                    .orElseGet(() -> {
                        MedicalRecord newRecord = new MedicalRecord();
                        patient.addMedicalRecord(newRecord);
                        return newRecord;
                    });
            record.setDisease(dto.getDisease());
            record.setMedications(dto.getMedications());
            record.setTreatmentHistory(dto.getTreatmentHistory());
            record.setDate(dto.getDate());
            record.setTreatment(dto.getTreatment());
        }}}


}
