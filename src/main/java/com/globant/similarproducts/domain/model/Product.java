package com.globant.similarproducts.domain.model;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.RequiredArgsConstructor;


@Getter
@RequiredArgsConstructor
public class Product {
         private final String id;
        private final String name;
        private final BigDecimal price;
        private final boolean availability;
    }



