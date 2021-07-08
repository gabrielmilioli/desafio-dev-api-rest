package com.milioli.gestaoContas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class GestaoContasApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(GestaoContasApplication.class, args);
	}

}
