package com.electronichealth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@Slf4j
@SpringBootApplication
@EnableCaching
public class ElectronicHealthRecordApplication {

	public static void main(String[] args) {
		log.info("ElectronicHealthRecordApplication about to start");
		SpringApplication.run(ElectronicHealthRecordApplication.class, args);
	}

}
