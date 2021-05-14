package com.vinod.microservices.best.practices;

import com.vinod.microservices.best.practices.config.aws.LocalStackProperties;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@OpenAPIDefinition(info = @Info(title = "Spring Microservice Best Practices", description = "Spring Boot microservice best practices.", version = "1.0"))
@EnableConfigurationProperties(LocalStackProperties.class)
@SpringBootApplication
public class SpringbootMicroserviceBestPracticesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootMicroserviceBestPracticesApplication.class, args);
	}

}
