package com.cbtl.paymentservice.config;

import dto.PaymentDto;
import event.order.OrderEvent;
import event.order.OrderStatus;
import event.payment.PaymentEvent;
import event.payment.PaymentStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Configuration
public class EventHandlerConfig {
    private static final Logger logger = LoggerFactory.getLogger(EventHandlerConfig.class);
    private final Map<UUID, Integer> bank;

    public EventHandlerConfig() {
        bank = new HashMap<>();
        bank.put(UUID.fromString("ed052d22-1d49-11ec-9621-0242ac130002"), 0);
        bank.put(UUID.fromString("d5b7fd66-1d12-11ec-9621-0242ac130002"), 1_000_000);
    }

    @Bean
    public Function<Flux<OrderEvent>, Flux<PaymentEvent>> inventoryProcessor() {
        return flux -> flux.flatMap(this::processPayment);
    }

    public Mono<PaymentEvent> processPayment(OrderEvent orderEvent) {
        logger.info("Processing OrderEvent ({}), eventId: {}", orderEvent.getOrderStatus(), orderEvent.getEventId());
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            var dto = PaymentDto.of(orderEvent.getOrder().getOrderId(), orderEvent.getOrder().getUserId(), orderEvent.getOrder().getPrice());
            if (bank.containsKey(orderEvent.getOrder().getUserId())) {
                if (bank.get(orderEvent.getOrder().getUserId()) > orderEvent.getOrder().getPrice()) {
                    bank.put(orderEvent.getOrder().getUserId(), bank.get(orderEvent.getOrder().getUserId()) - orderEvent.getOrder().getPrice());
                    return Mono.just(new PaymentEvent(dto, PaymentStatus.RESERVED));
                }
            }
            return Mono.just(new PaymentEvent(dto, PaymentStatus.REJECTED));
        }
        if (bank.containsKey(orderEvent.getOrder().getUserId())) {
            bank.put(orderEvent.getOrder().getUserId(), bank.get(orderEvent.getOrder().getUserId()) + orderEvent.getOrder().getPrice());
        }
        return Mono.empty();
    }
}
