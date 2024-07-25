package com.electronichealth.kafka;

import com.electronichealth.dto.PatientDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ConsumerTest {

    @InjectMocks
    Consumer consumer;

    @Test
    public void testReceive() {
        PatientDTO patientDTO = new PatientDTO(1L,"John Doe", "1990-01-01",null);
        consumer.listen(patientDTO);
    }
}
