package com.globant.similarproducts.infrastructure.repository;
import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.infrastructure.client.dto.ExternalProductDetailDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
public class ProductMapperTest {
    private final ProductMapper mapper = new ProductMapper();

    @Test
    void shouldMapExternalDtoToDomainProduct() {
        ExternalProductDetailDto dto = new ExternalProductDetailDto();
        dto.setId("2");
        dto.setName("Dress");
        dto.setPrice(new BigDecimal("19.99"));
        dto.setAvailability(true);

        Product p = mapper.toDomain(dto);

        assertEquals("2", p.getId());
        assertEquals("Dress", p.getName());
        assertEquals(new BigDecimal("19.99"), p.getPrice());
        assertTrue(p.isAvailability());
    }
}
