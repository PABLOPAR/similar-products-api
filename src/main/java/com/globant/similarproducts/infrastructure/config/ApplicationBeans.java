package com.globant.similarproducts.infrastructure.config;

import com.globant.similarproducts.application.usecase.GetSimilarProductsUseCase;
import com.globant.similarproducts.domain.repository.ProductRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationBeans {
    @Bean
    public GetSimilarProductsUseCase getSimilarProductsUseCase(ProductRepository productRepository) {
        return new GetSimilarProductsUseCase(productRepository);
    }

}
