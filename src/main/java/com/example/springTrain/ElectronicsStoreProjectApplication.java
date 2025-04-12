package com.example.springTrain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ElectronicsStoreProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(ElectronicsStoreProjectApplication.class, args);
	}
}
