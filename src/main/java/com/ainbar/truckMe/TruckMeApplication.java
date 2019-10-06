package com.ainbar.truckMe;

import com.ainbar.truckMe.service.BatchServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@SpringBootApplication
@EnableScheduling
@Slf4j
public class TruckMeApplication {

	@Autowired
	BatchServiceImpl batchService;

	public static void main(String[] args) {
		SpringApplication.run(TruckMeApplication.class, args);
	}

	@Scheduled(cron = "0 55 17 * * ?")
	public void run() {
		log.info("Batch processing ...");
		batchService.getRecords();
	}
}
