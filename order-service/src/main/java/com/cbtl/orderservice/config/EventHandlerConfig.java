package com.cbtl.orderservice.config;

import com.cbtl.orderservice.service.OrderStatusUpdateHandler;
import event.inventory.InventoryEvent;
import event.payment.PaymentEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class EventHandlerConfig {
    private final OrderStatusUpdateHandler orderStatusUpdateHandler;

    @Autowired
    public EventHandlerConfig(OrderStatusUpdateHandler orderStatusUpdateHandler) {
        this.orderStatusUpdateHandler = orderStatusUpdateHandler;
    }

    @Bean
    public Consumer<PaymentEvent> paymentEventConsumer() {
        return pe -> {
            orderStatusUpdateHandler.updateOrder(pe.getPayment().getOrderId(), po -> {
                po.setPaymentStatus(pe.getPaymentStatus());
            });
        };
    }

    @Bean
    public Consumer<InventoryEvent> inventoryEventConsumer() {
        return ie -> {
            orderStatusUpdateHandler.updateOrder(ie.getInventory().getOrderId(), po -> {
                po.setInventoryStatus(ie.getStatus());
            });
        };
    }
}
