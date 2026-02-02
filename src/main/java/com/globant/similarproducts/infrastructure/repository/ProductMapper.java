package com.globant.similarproducts.infrastructure.repository;

import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.infrastructure.client.dto.ExternalProductDetailDto;
public class ProductMapper {
    Product toDomain(ExternalProductDetailDto dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.isAvailability()
        );
    }
}
