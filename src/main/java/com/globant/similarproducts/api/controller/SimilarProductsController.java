package com.globant.similarproducts.api.controller;

import com.globant.similarproducts.api.dto.ProductDetailResponse;
import com.globant.similarproducts.application.usecase.GetSimilarProductsUseCase;
import com.globant.similarproducts.domain.exception.ProductNotFoundException;
import com.globant.similarproducts.domain.model.Product;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/product")
public class SimilarProductsController {

    private final GetSimilarProductsUseCase useCase;

    public SimilarProductsController(GetSimilarProductsUseCase useCase) {
        this.useCase = useCase;
    }

    @GetMapping("/{productId}/similar")
    public List<ProductDetailResponse> getSimilar(@PathVariable String productId) {
        return useCase.execute(productId).stream()
                .map(p -> new ProductDetailResponse(
                        p.getId(),
                        p.getName(),
                        p.getPrice(),
                        p.isAvailability()
                ))
                .toList();
    }
}
