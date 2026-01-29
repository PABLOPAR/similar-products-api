package com.globant.similarproducts.infrastructure.config;

import com.globant.similarproducts.infrastructure.client.ExistingProductApiClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientBeans {
    @Bean
    public ExistingProductApiClient existingProductApiClient(WebClient externalProductApiWebClient) {
        return new ExistingProductApiClient(externalProductApiWebClient);
    }
}
