package com.cbtl.orderservice.service;

import com.cbtl.orderservice.client.PriceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
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
        return priceClient.getPrice(productId)
                .flatMap(value -> {
                    if (ObjectUtils.isEmpty(value)) {
                        return Mono.empty();
                    }
                    return Mono.just(Integer.valueOf(value));
                });
    }
}
