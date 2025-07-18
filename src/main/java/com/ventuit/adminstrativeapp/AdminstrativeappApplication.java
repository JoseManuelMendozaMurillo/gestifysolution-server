package com.ventuit.adminstrativeapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

@SpringBootApplication
public class AdminstrativeappApplication {

	public static void main(String[] args) {
		SpringApplication.run(AdminstrativeappApplication.class, args);
	}

	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
				.info(new Info()
						.title("Gestifysolution API")
						.version("1.0"));
	}
}
