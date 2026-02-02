package com.globant.similarproducts.infrastructure.client;

import com.globant.similarproducts.domain.exception.UpstreamServiceException;
import com.globant.similarproducts.infrastructure.client.dto.ExternalProductDetailDto;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import com.globant.similarproducts.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.client.WebClientRequestException;
import java.util.concurrent.TimeoutException;
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

                    return response.bodyToMono(new ParameterizedTypeReference<List<String>>() {});
                })
                .onErrorMap(
                        ex -> ex instanceof WebClientRequestException
                                || ex instanceof java.util.concurrent.TimeoutException,
                        ex -> new UpstreamServiceException(
                                "Upstream unreachable calling similarids for: " + productId, ex
                        )
                )
                .block();

        return ids == null ? List.of() : ids;
    }


    public Mono<ExternalProductDetailDto> getProductDetail(String productId) {
        return webClient.get()
                .uri("/product/{productId}", productId)
                .exchangeToMono(response -> {
                    HttpStatusCode status = response.statusCode();

                    if (status.value() == 404) {
                        return Mono.error(new ProductNotFoundException(productId));
                    }

                    if (status.isError()) {
                        return response.createException().flatMap(Mono::error);
                    }

                    return response.bodyToMono(ExternalProductDetailDto.class);
                })
                .onErrorMap(
                        ex -> ex instanceof WebClientRequestException || isTimeout(ex),
                        ex -> new UpstreamServiceException("Upstream unreachable calling product detail for: " + productId, ex)
                );
    }

    private boolean isTimeout(Throwable ex) {
        Throwable t = ex;
        while (t != null) {
            if (t instanceof TimeoutException) return true;
            t = t.getCause();
        }
        return false;
    }


}
