package com.ainbar.truckMe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TruckMeApplication {
    public static void main(String[] args) {
        SpringApplication.run(TruckMeApplication.class, args);
    }
}
