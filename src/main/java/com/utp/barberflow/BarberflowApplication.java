package com.utp.barberflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class BarberflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(BarberflowApplication.class, args);
	}

}
