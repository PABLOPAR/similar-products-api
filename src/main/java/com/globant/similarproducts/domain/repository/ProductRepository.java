package com.globant.similarproducts.domain.repository;
import com.globant.similarproducts.domain.model.Product;
import java.util.List;
import java.util.Optional;
public interface ProductRepository {

    /**
     * @return ids de productos similares en orden de similitud.
     * @throws com.globant.similarproducts.domain.exception.ProductNotFoundException si el producto base no existe
     */
    List<String> findSimilarIds(String productId);

    /**
     * Si un producto similar falla o no existe, devolvemos Optional.empty() para evitar un nullPointer. Si no
     * existe lo omitimos
     */
    Optional<Product> findProductById(String productId);
}
