package com.cbtl.orderservice.client;

import com.cbtl.orderservice.error.PriceClientException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;
import java.util.function.Predicate;

@Component
public class PriceClient {
    private static final Logger logger = LoggerFactory.getLogger(PriceClient.class);
    private final WebClient webClient;

    public PriceClient(WebClient.Builder builder, @Value("${inventoryService.port}") Integer inventoryServicePort) {
        this.webClient = builder.baseUrl(String.format("http://localhost:%d", inventoryServicePort)).build();
    }

    public Mono<String> getPrice(UUID productId) {
        return this.webClient
                .get()
                .uri("/price/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono.error(new PriceClientException(clientResponse.rawStatusCode())))
                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono.error(new PriceClientException(clientResponse.rawStatusCode())))
                .bodyToMono(String.class)
                .onErrorMap(Predicate.not(PriceClientException.class::isInstance), throwable -> {
                    logger.error("Error: " + throwable.getMessage());
                    return throwable;
                });
    }
}
