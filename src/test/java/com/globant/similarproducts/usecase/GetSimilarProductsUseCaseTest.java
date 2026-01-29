package com.globant.similarproducts.usecase;

import com.globant.similarproducts.application.usecase.GetSimilarProductsUseCase;
import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.domain.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
public class GetSimilarProductsUseCaseTest {
    private ProductRepository productRepository;
    private GetSimilarProductsUseCase useCase;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        useCase = new GetSimilarProductsUseCase(productRepository);
    }

    @Test
    void execute_shouldReturnProductsInSameOrderAsSimilarIds_andSkipMissingOnes() {
        // given
        String productId = "1";
        List<String> similarIds = List.of("2", "3", "4");

        Product p2 = new Product("2", "Dress", new BigDecimal("19.99"), true);
        // simulamos que el 3 "falla" o no está disponible => Optional.empty()
        Product p4 = new Product("4", "Boots", new BigDecimal("39.99"), true);

        when(productRepository.findSimilarIds(productId)).thenReturn(similarIds);
        when(productRepository.findProductById("2")).thenReturn(Optional.of(p2));
        when(productRepository.findProductById("3")).thenReturn(Optional.empty());
        when(productRepository.findProductById("4")).thenReturn(Optional.of(p4));

        // when
        List<Product> result = useCase.execute(productId);

        // then
        assertEquals(2, result.size());
        assertEquals("2", result.get(0).getId());
        assertEquals("4", result.get(1).getId());

        // además verificamos interacción (orden y llamadas)
        verify(productRepository).findSimilarIds(productId);
        verify(productRepository).findProductById("2");
        verify(productRepository).findProductById("3");
        verify(productRepository).findProductById("4");
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void execute_shouldReturnEmptyList_whenNoSimilarIds() {
        // given
        String productId = "1";
        when(productRepository.findSimilarIds(productId)).thenReturn(List.of());

        // when
        List<Product> result = useCase.execute(productId);

        // then
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(productRepository).findSimilarIds(productId);
        verifyNoMoreInteractions(productRepository);
    }
}