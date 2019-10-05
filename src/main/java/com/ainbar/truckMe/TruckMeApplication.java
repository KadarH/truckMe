package com.ainbar.truckMe;

import com.ainbar.truckMe.service.BatchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class TruckMeApplication implements CommandLineRunner {

	@Autowired
	BatchServiceImpl batchService;

	public static void main(String[] args) {
		SpringApplication.run(TruckMeApplication.class, args);
	}

	@Override
	public void run(String... args) {
		log.info("Batch processing ...");
		batchService.getRecords();
	}
}
