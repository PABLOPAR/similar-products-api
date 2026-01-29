package com.globant.similarproducts.application.usecase;

import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.domain.repository.ProductRepository;
import java.util.List;


public class GetSimilarProductsUseCase {
    private final ProductRepository productRepository;

    public GetSimilarProductsUseCase(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * Reglas:
     * - Mantener el orden de similaridad.
     * - Si el producto base no existe -> excepción (luego controller lo mapeará a 404).
     * - Si un producto similar no está disponible / falla -> se omite (degradación).
     */
    public List<Product> execute(String productId) {
        List<String> similarIds = productRepository.findSimilarIds(productId);

        List<Product> result = similarIds.stream()
                .map(productRepository::findProductById)   // Optional<Product>
                .flatMap(java.util.Optional::stream)       // filtra empties
                .toList();

        return result;
    }

}
