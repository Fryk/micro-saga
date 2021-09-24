package com.cbtl.inventoryservice.config;

import com.cbtl.inventoryservice.service.InventoryService;
import event.inventory.InventoryEvent;
import event.order.OrderEvent;
import event.order.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Configuration
public class EventHandlerConfig {
    private static final Logger logger = LoggerFactory.getLogger(EventHandlerConfig.class);
    private final InventoryService inventoryService;

    @Autowired
    public EventHandlerConfig(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @Bean
    public Function<Flux<OrderEvent>, Flux<InventoryEvent>> inventoryProcessor() {
        return flux -> flux.flatMap(this::processInventory);
    }

    public Mono<InventoryEvent> processInventory(OrderEvent orderEvent) {
        logger.info("Processing OrderEvent ({}), eventId: {}", orderEvent.getOrderStatus(), orderEvent.getEventId());
        if (orderEvent.getOrderStatus().equals(OrderStatus.ORDER_CREATED)) {
            return this.inventoryService.newOrder(orderEvent);
        }
        return this.inventoryService.cancelOrder(orderEvent);
    }
}
