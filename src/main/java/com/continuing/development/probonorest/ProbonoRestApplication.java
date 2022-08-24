package com.continuing.development.probonorest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class ProbonoRestApplication {
	private final static String PATH_PATTERN = "/**";
	private final static String ALLOWED_ORIGIN = "http://localhost:4200";

	public static void main(String[] args) {
		SpringApplication.run(ProbonoRestApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry
						.addMapping(PATH_PATTERN)
						.allowedMethods("POST", "GET", "PUT", "DELETE","HEAD","OPTIONS","PATCH")
						.allowedOrigins(ALLOWED_ORIGIN)
						.maxAge(0);
			}
		};
	}
}
