package com.cbtl.orderservice.service;

import com.cbtl.orderservice.entity.Order;
import dto.OrderDto;
import event.order.OrderEvent;
import event.order.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Sinks;

@Service
public class OrderStatusPublisher {
    private final Sinks.Many<OrderEvent> orderSink;

    @Autowired
    public OrderStatusPublisher(Sinks.Many<OrderEvent> orderSink) {
        this.orderSink = orderSink;
    }

    public void raiseOrderEvent(final Order order, OrderStatus orderStatus) {
        var dto = OrderDto.of(
                order.getId(),
                order.getProductId(),
                order.getUserId(),
                order.getPrice()
        );

        var orderEvent = new OrderEvent(dto, orderStatus);
        this.orderSink.tryEmitNext(orderEvent);
    }
}
