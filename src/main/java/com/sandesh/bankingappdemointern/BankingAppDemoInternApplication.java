package com.sandesh.bankingappdemointern;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Bank App API",
				description = "Backend REST APIs for a Banking Application",
				version = "v1.0",
				contact = @Contact(
						name = "Sandesh Shrestha",
						email = "sandeshshrestha4313@gmail.com",
						url = "https://github.com/sandesh4313/banking_app_intern"
				),
				license = @License(
						name = "This is Banking App Demo",
						url = "https://github.com/sandesh4313/banking_app_intern"
				)
		),
				externalDocs = @ExternalDocumentation(
						description = "Documentation for Banking App APIs",
						url = "https://github.com/sandesh4313/banking_app_intern"
				)
		)


public class BankingAppDemoInternApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankingAppDemoInternApplication.class, args);
	}

}
