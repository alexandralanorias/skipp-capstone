package com.skipp.enlistment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc;

@SpringBootApplication

// TODO What annotation enables transaction management?
@EnableTransactionManagement
// TODO What annotation enables security annotations?
@EnableMethodSecurity
//@EnableSwagger2WebMvc
public class EnlistmentApplication {
	public static void main(String[] args) {
		SpringApplication.run(EnlistmentApplication.class, args);
	}
}
