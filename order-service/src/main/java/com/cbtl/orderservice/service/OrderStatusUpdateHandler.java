package com.cbtl.orderservice.service;

import com.cbtl.orderservice.entity.Order;
import com.cbtl.orderservice.repository.OrderRepository;
import event.inventory.InventoryStatus;
import event.order.OrderStatus;
import event.payment.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.UUID;
import java.util.function.*;

@Service
public class OrderStatusUpdateHandler {
    private final OrderStatusPublisher publisher;
    private final OrderRepository orderRepository;

    @Autowired
    public OrderStatusUpdateHandler(OrderStatusPublisher publisher, OrderRepository repository) {
        this.publisher = publisher;
        this.orderRepository = repository;
    }

    @Transactional
    public Mono<Order> updateOrder(final UUID orderId, BiFunction<OrderRepository, UUID, Mono<Void>> updateFunction){
        return updateFunction.apply(orderRepository, orderId)
                .then(this.orderRepository.findById(orderId))
                .map(order -> {
                    this.updateOrder(order);
                    return order;
                })
                .flatMap(order -> {
                    return orderRepository.save(order);
                });
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
