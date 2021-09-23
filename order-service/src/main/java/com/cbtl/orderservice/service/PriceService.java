package com.cbtl.orderservice.service;

import com.cbtl.orderservice.client.PriceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class PriceService {
    private final PriceClient priceClient;

    @Autowired
    public PriceService(PriceClient priceClient) {
        this.priceClient = priceClient;
    }

    public final Mono<Integer> getPrice(UUID productId) {
        return priceClient.getPrice(productId).map(Integer::valueOf);
    }
}
