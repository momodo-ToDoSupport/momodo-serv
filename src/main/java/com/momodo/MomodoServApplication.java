package com.momodo;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MomodoServApplication {

	public static void main(String[] args) {
		SpringApplication.run(MomodoServApplication.class, args);
	}

}
