package com.globant.similarproducts.infrastructure.repository;
import com.globant.similarproducts.domain.model.Product;
import com.globant.similarproducts.domain.repository.ProductRepository;
import com.globant.similarproducts.infrastructure.client.ExistingProductApiClient;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@Repository
public class ProductRepositoryImpl implements ProductRepository {
    private static final Logger log = LoggerFactory.getLogger(ProductRepositoryImpl.class);

    private final ExistingProductApiClient apiClient;
    private final ProductMapper mapper = new ProductMapper(); // ✅ añadido

    public ProductRepositoryImpl(ExistingProductApiClient apiClient) {
        this.apiClient = apiClient;
    }

    @Override
    public List<String> findSimilarIds(String productId) {
        return apiClient.getSimilarIds(productId);
    }

    @Override
    public Optional<Product> findProductById(String productId) {
        try {
            var dto = apiClient.getProductDetail(productId).block();

            if (dto == null) {
                log.debug("Product detail not found for id={}", productId);
                return Optional.empty();
            }

            log.debug("getProductDetail({}) -> {}", productId, dto);

            return Optional.of(mapper.toDomain(dto)); // ✅ aquí
        } catch (Exception ex) {
            log.warn("Error calling external product API for id={}", productId, ex);
            return Optional.empty(); // degradación controlada
        }
    }

}
