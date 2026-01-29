package com.globant.similarproducts.infrastructure.client;

import com.globant.similarproducts.domain.exception.UpstreamServiceException;
import com.globant.similarproducts.infrastructure.client.dto.ExternalProductDetailDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.globant.similarproducts.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatusCode;

import java.util.List;

public class ExistingProductApiClient {

    private final WebClient webClient;

    public ExistingProductApiClient(WebClient externalProductApiWebClient) {
        this.webClient = externalProductApiWebClient;
    }

    public List<String> getSimilarIds(String productId) {
        List<String> ids = webClient.get()
                .uri("/product/{productId}/similarids", productId)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.value() == 404) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    if (status.isError()) {
                        return response.createException().flatMap(Mono::error);
                    }

                    // el mock devuelve ["2","3","4"] o [2,3,4] según versión
                    return response.bodyToMono(
                            new ParameterizedTypeReference<List<String>>() {}
                    );
                })
                .block();

        return ids == null ? List.of() : ids;
    }


    public Mono<ExternalProductDetailDto> getProductDetail(String productId) {
        return webClient.get()
                .uri("/product/{productId}", productId)
                .exchangeToMono(response -> {
                    if (response.statusCode().value() == 404) {
                        return Mono.empty();
                    }
                    if (response.statusCode().isError()) {
                        return response.createException()
                                .flatMap(ex -> Mono.error(new UpstreamServiceException(
                                        "Upstream error getting product: " + productId, ex)));
                    }
                    return response.bodyToMono(ExternalProductDetailDto.class);
                });
    }
}
