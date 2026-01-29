package com.globant.similarproducts.infrastructure.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "external.product-api")
public class ExternalProductApiProperties {
    private String baseUrl;
    private int timeoutMs = 2000;

};
