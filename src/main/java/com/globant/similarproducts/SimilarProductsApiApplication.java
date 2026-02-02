package com.globant.similarproducts;

import com.globant.similarproducts.infrastructure.config.ExternalProductApiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;


@EnableConfigurationProperties(ExternalProductApiProperties.class)
@SpringBootApplication
public class SimilarProductsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimilarProductsApiApplication.class, args);
	}

}
