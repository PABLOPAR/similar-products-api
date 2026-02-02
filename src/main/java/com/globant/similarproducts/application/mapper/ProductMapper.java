package com.globant.similarproducts.application.mapper;

import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.infrastructure.client.dto.ExternalProductDetailDto;
public class ProductMapper {

    public Product toDomain(ExternalProductDetailDto dto) {
        return new Product(
                dto.getId(),
                dto.getName(),
                dto.getPrice(),
                dto.isAvailability()
        );
    }

}
