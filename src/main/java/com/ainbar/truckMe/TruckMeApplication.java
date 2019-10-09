package com.ainbar.truckMe;

import com.ainbar.truckMe.service.impl.BatchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class TruckMeApplication {

	@Autowired
	BatchServiceImpl batchService;

	public static void main(String[] args) {
		SpringApplication.run(TruckMeApplication.class, args);
	}


}
