package com.electronichealth.kafka;

import com.electronichealth.dto.PatientDTO;
import com.electronichealth.entity.Patient;
import com.electronichealth.utility.PatientMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Slf4j
@Component
public class Producer {

    @Autowired
    private KafkaTemplate<String, PatientDTO> kafkaTemplate;
    public void sendToKafka(Patient patient) {
        PatientDTO patientDTO = PatientMapper.convertToDTO(patient);
        log.info("Preparing event to sent : {}", patientDTO);
        CompletableFuture<SendResult<String, PatientDTO>> completableFuture = kafkaTemplate.send("demoTopic", patientDTO);
        completableFuture.whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("Sent message : {} with offset : {}", patientDTO, result.getRecordMetadata().offset());
            } else {
                log.info("Unable to send message : {} due to : {}", patientDTO, ex.getMessage());
                throw new KafkaException(ex.getMessage());
            }
        });
    }
}
