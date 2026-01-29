package com.globant.similarproducts.infrastructure.client.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
public class ExternalProductDetailDto {
    private String id;
    private String name;
    private BigDecimal price;
    private boolean availability;
}
