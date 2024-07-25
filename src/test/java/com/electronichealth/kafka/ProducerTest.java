package com.electronichealth.kafka;


import static org.mockito.Mockito.*;
import com.electronichealth.dto.PatientDTO;
import com.electronichealth.entity.Patient;
import com.electronichealth.utility.PatientMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;

import java.util.concurrent.CompletableFuture;

@ExtendWith(MockitoExtension.class)
public class ProducerTest {

    @Mock
    private KafkaTemplate<String, PatientDTO> kafkaTemplate;

    @InjectMocks
    private Producer producer;

    @Test
    void testSendToKafka_Success() {
        Patient patient = new Patient("John Doe", "1990-01-01");
        patient.setId(1L);
        PatientDTO patientDTO = PatientMapper.convertToDTO(patient);
        CompletableFuture<SendResult<String, PatientDTO>> future = new CompletableFuture<>();
        SendResult<String, PatientDTO> sendResult = mock(SendResult.class);

        when(kafkaTemplate.send(anyString(), any(PatientDTO.class))).thenReturn(future);
        future.complete(sendResult);
        producer.sendToKafka(patient);
        verify(kafkaTemplate).send("demoTopic", patientDTO);
    }

    @Test
    void testSendToKafka_Exception() {
        Patient patient = new Patient("John Doe", "1990-01-01");
        patient.setId(1L);
        PatientDTO patientDTO = PatientMapper.convertToDTO(patient);
        CompletableFuture<SendResult<String, PatientDTO>> future = new CompletableFuture<>();
        when(kafkaTemplate.send(anyString(), any(PatientDTO.class))).thenReturn(future);
        future.completeExceptionally(new RuntimeException("Kafka exception"));
        producer.sendToKafka(patient);
        verify(kafkaTemplate).send("demoTopic", patientDTO);

    }
}