package com.Back.PorscheDashboardBackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PorscheDashboardBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(PorscheDashboardBackendApplication.class, args);
	}

}
