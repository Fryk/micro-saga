package com.cbtl.orderservice.service;

import com.cbtl.orderservice.entity.Order;
import com.cbtl.orderservice.repository.OrderRepository;
import event.inventory.InventoryStatus;
import event.order.OrderStatus;
import event.payment.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;

@Service
public class OrderStatusUpdateHandler {
    private final OrderStatusPublisher publisher;
    private final OrderRepository repository;

    @Autowired
    public OrderStatusUpdateHandler(OrderStatusPublisher publisher, OrderRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    @Transactional
    public void updateOrder(final UUID orderId, Consumer<Order> consumer){
        this.repository.findById(orderId).map(order -> consumer.andThen(this::updateOrder));

    }

    private void updateOrder(Order order) {
        if (Objects.isNull(order.getInventoryStatus()) || Objects.isNull(order.getPaymentStatus())) {
            return;
        }
        var isComplete = PaymentStatus.RESERVED.equals(order.getPaymentStatus()) && InventoryStatus.RESERVED.equals(order.getInventoryStatus());
        var orderStatus = isComplete ? OrderStatus.ORDER_COMPLETED : OrderStatus.ORDER_CANCELLED;
        order.setOrderStatus(orderStatus);
        if (!isComplete) {
            this.publisher.raiseOrderEvent(order, orderStatus);
        }
    }
}
