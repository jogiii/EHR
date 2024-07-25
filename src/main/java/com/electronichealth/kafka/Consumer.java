package com.electronichealth.kafka;

import com.electronichealth.dto.PatientDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Consumer {
    @KafkaListener(topics = "demoTopic", groupId = "my-group",containerFactory = "kafkaListenerContainerFactory")
    public void listen(PatientDTO message) {
        log.info("Received message: " + message);
    }
}
