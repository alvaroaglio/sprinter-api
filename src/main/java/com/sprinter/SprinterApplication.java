package com.sprinter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.sprinter.persistence.repository.ProductRepository;
import com.sprinter.service.ProductService;
import com.sprinter.service.impl.ProductServiceImpl;

/**
 * 
 * @author Álvaro Aglio Sánchez
 *
 */
@SpringBootApplication
@EnableJpaRepositories
@EnableScheduling
public class SprinterApplication {

	public static void main(String[] args) {
		SpringApplication.run(SprinterApplication.class, args);
	}

	@Bean
	public ProductService productService(ProductRepository productRepository) {
		return new ProductServiceImpl(productRepository);
	}

}
