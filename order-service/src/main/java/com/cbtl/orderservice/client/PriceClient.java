package com.cbtl.orderservice.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Component
public class PriceClient {
    private final WebClient webClient;

    public PriceClient(WebClient.Builder builder, @Value("inventoryService.port") Integer inventoryServicePort) {
        this.webClient = builder.baseUrl(String.format("http://localhost:%d", inventoryServicePort)).build();
    }

    public Mono<String> getPrice(UUID productId) {
        return this.webClient
                .get()
                .uri("/price/" + productId)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(String.class);
    }
}
