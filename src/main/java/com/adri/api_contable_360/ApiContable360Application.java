package com.adri.api_contable_360;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ApiContable360Application {

	public static void main(String[] args) {
		SpringApplication.run(ApiContable360Application.class, args);
	}

}
