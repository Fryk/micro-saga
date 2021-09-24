package com.cbtl.orderservice.config;

import com.cbtl.orderservice.repository.OrderRepository;
import com.cbtl.orderservice.service.OrderStatusUpdateHandler;
import event.inventory.InventoryEvent;
import event.payment.PaymentEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventHandlerConfig {
    public static final Logger logger = LoggerFactory.getLogger(EventHandlerConfig.class);
    private final OrderStatusUpdateHandler orderStatusUpdateHandler;

    @Autowired
    public EventHandlerConfig(OrderStatusUpdateHandler orderStatusUpdateHandler) {
        this.orderStatusUpdateHandler = orderStatusUpdateHandler;
    }

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return pe -> {
            logger.info("Received PaymentEvent " + pe.getEventId());
            orderStatusUpdateHandler.updateOrder(pe.getPayment().getOrderId(), (repository, orderId) -> {
                return repository.setPaymentStatus(pe.getPaymentStatus(), orderId);
            }).subscribe();
        };
    }

    @Bean
    public Consumer<InventoryEvent> inventoryEventConsumer() {
        return ie -> {
            logger.info("Received InventoryEvent " + ie.getEventId());
            orderStatusUpdateHandler.updateOrder(ie.getInventory().getOrderId(), (repository, orderId) -> {
                return repository.setInventoryStatus(ie.getStatus(), orderId);
            }).subscribe();
        };
    }
}
