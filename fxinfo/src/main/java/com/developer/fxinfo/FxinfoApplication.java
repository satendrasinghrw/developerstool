package com.ubs.fxinfo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Configuration
@EnableWebMvc
public class FxinfoApplication {

	public static void main(String[] args) {
		SpringApplication.run(FxinfoApplication.class, args);
	}
}
