package com.electronichealth.controller;

import com.electronichealth.dto.PatientDTO;
import com.electronichealth.service.IPatientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class PatientControllerTest {
    private MockMvc mockMvc;
    @Mock
    private IPatientService patientService;

    @InjectMocks
    private PatientController patientController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(patientController).build();
    }

    @Test
    public void testGetPatientRecord() throws Exception {
        Long patientId = 1L;
        PatientDTO patientDTO = new PatientDTO(patientId, "John Doe", "1990-01-01", null);
        when(patientService.getPatientRecordAsync(patientId)).thenReturn(CompletableFuture.completedFuture(patientDTO));

        mockMvc.perform(get("/api/v1/patients/getPatientRecord")
                        .param("patientId", String.valueOf(patientId))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void testCreatePatientRecord() throws Exception {
        PatientDTO patientDTO = new PatientDTO(null, "Jane Doe", "1992-02-01", null);
        PatientDTO returnedDTO = new PatientDTO(1L, "Jane Doe", "1992-02-01", null);
        when(patientService.createPatientRecordAsync(patientDTO)).thenReturn(CompletableFuture.completedFuture(returnedDTO));

        mockMvc.perform(post("/api/v1/patients/createPatientRecord")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(patientDTO))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

    }

    @Test
    public void testUpdatePatientRecord_Success() throws Exception {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String birthDate = dateFormat.format(new Date());
        Long patientId = 1L;
        PatientDTO patientDTO = new PatientDTO(patientId, "Updated Name", birthDate, null);
        when(patientService.updatePatientAsync(patientId, patientDTO)).thenReturn(CompletableFuture.completedFuture(patientDTO));
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonContent = objectMapper.writeValueAsString(patientDTO);
        mockMvc.perform(put("/api/v1/patients/updatePatientRecord")
                        .param("patientId", patientId.toString())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}