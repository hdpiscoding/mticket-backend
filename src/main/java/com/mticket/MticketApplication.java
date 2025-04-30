package com.mticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MticketApplication {

	public static void main(String[] args) {
		SpringApplication.run(MticketApplication.class, args);
	}

}
