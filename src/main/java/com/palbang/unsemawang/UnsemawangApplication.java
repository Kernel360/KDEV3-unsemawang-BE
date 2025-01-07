package com.palbang.unsemawang;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class UnsemawangApplication {

	public static void main(String[] args) {
		SpringApplication.run(UnsemawangApplication.class, args);
	}
}
