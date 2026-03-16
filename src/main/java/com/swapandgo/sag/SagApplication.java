package com.swapandgo.sag;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.core.env.Environment;

@SpringBootApplication
@EnableAsync
public class SagApplication {

	@Autowired
	private Environment env;

	public static void main(String[] args) {
		SpringApplication.run(SagApplication.class, args);
	}

	@PostConstruct
	public void logEnv() {
		System.out.println("spring.datasource.url = " + env.getProperty("spring.datasource.url"));
		System.out.println("url = " + env.getProperty("spring.datasource.url"));
	}

}
