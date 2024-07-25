package com.electronichealth.service;

import com.electronichealth.dto.MedicalRecordDTO;
import com.electronichealth.dto.PatientDTO;
import com.electronichealth.entity.Patient;
import com.electronichealth.kafka.Producer;
import com.electronichealth.repository.PatientRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PatientServiceTest {

    @Mock
    private PatientRepository patientRepository;

    @InjectMocks
    private PatientService patientService;

    @Mock
    Producer producer;


    @Test
    void testGetPatientRecordAsync_Found() {
        Long patientId = 1L;
        Patient patient = new Patient("John Doe", "1990-01-01");
        patient.setId(1L);
        PatientDTO expectedDTO = new PatientDTO(1L, "John Doe", "1990-01-01", new ArrayList<>());

        when(patientRepository.findByIdWithMedicalRecords(patientId)).thenReturn(Optional.of(patient));
        CompletableFuture<PatientDTO> result = patientService.getPatientRecordAsync(patientId);

        assertAll(
                () -> assertNotNull(result),
                () -> assertEquals(expectedDTO, result.join())
        );
    }

    @Test
    void testGetPatientRecordAsync_NotFound() {
        Long patientId = 1L;
        when(patientRepository.findByIdWithMedicalRecords(patientId)).thenReturn(Optional.empty());

        CompletableFuture<PatientDTO> result = patientService.getPatientRecordAsync(patientId);

        assertThrows(CompletionException.class, result::join);
    }

    @Test
    void testCreatePatientRecordAsync_Success() throws ExecutionException, InterruptedException {
        PatientDTO inputDTO = new PatientDTO(null, "Jane Doe", "1990-01-01", null);
        Patient patient = new Patient("Jane Doe", "1990-01-01");
        List<MedicalRecordDTO> dtoList = new ArrayList<>();
        dtoList.add(new MedicalRecordDTO(1L,"Allergy","Anti Histamines","07/11/2024","Asthma","anti histamines"));

        PatientDTO expectedDTO = new PatientDTO(1L, "Jane Doe", "1990-01-01", dtoList);

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        CompletableFuture<PatientDTO> resultFuture = patientService.createPatientRecordAsync(inputDTO);

        PatientDTO resultDTO = resultFuture.get();
        assertNotNull(resultDTO);
        assertEquals(expectedDTO.getName(), resultDTO.getName());
        assertEquals(expectedDTO.getDOB(), resultDTO.getDOB());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }

    @Test
    void testUpdatePatientAsync_Success() throws ExecutionException, InterruptedException {
        Long patientId = 1L;
        List<MedicalRecordDTO> dtoList = new ArrayList<>();
        dtoList.add(new MedicalRecordDTO(1L,"Allergy","Anti Histamines","07/11/2024","Asthma","anti histamines"));

        PatientDTO inputDTO = new PatientDTO(patientId, "John Doe Updated", "1990-01-02", dtoList);
        Patient existingPatient = new Patient("John Doe", "1990-01-01");
        Patient updatedPatient = new Patient("John Doe Updated", "1990-01-02");
        updatedPatient.setId(patientId);

        when(patientRepository.findById(patientId)).thenReturn(Optional.of(existingPatient));
        when(patientRepository.save(any(Patient.class))).thenReturn(updatedPatient);

        doNothing().when(producer).sendToKafka(any());

        CompletableFuture<PatientDTO> resultFuture = patientService.updatePatientAsync(patientId, inputDTO);

        PatientDTO resultDTO = resultFuture.get();
        assertNotNull(resultDTO);
        assertEquals(inputDTO.getName(), resultDTO.getName());
        assertEquals(inputDTO.getDOB(), resultDTO.getDOB());
        verify(patientRepository, times(1)).save(any(Patient.class));
    }
}