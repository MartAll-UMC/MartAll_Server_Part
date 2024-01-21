package com.backend.martall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class MartallApplication {
	public static void main(String[] args) {
		SpringApplication.run(MartallApplication.class, args);
	}
}
