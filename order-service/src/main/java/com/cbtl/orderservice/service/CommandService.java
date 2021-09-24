package com.cbtl.orderservice.service;

import com.cbtl.orderservice.entity.Order;
import com.cbtl.orderservice.repository.OrderRepository;
import dto.OrderRequestDto;
import event.inventory.InventoryStatus;
import event.order.OrderStatus;
import event.payment.PaymentStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

@Service
public class CommandService {
    private final OrderRepository orderRepository;
    private final OrderStatusPublisher orderStatusPublisher;
    private final PriceService priceService;

    @Autowired
    public CommandService(OrderRepository orderRepository, OrderStatusPublisher orderStatusPublisher, PriceService priceService) {
        this.orderRepository = orderRepository;
        this.orderStatusPublisher = orderStatusPublisher;
        this.priceService = priceService;
    }

    @Transactional
    public Mono<Order> createOrder(OrderRequestDto orderRequestDto) {
        return this.priceService.getPrice(orderRequestDto.getProductId())
                .map(price -> this.dtoToEntity(orderRequestDto, price, true))
                .flatMap(this.orderRepository::save)
                .map(order -> {
                    this.orderStatusPublisher.raiseOrderEvent(order, OrderStatus.ORDER_CREATED);
                    return order;
                });
    }

    private Order dtoToEntity(OrderRequestDto orderRequestDto, Integer price, Boolean isNew) {
        Order order = new Order();
        order.setId(orderRequestDto.getOrderId());
        order.setProductId(orderRequestDto.getProductId());
        order.setUserId(orderRequestDto.getUserId());
        order.setOrderStatus(OrderStatus.ORDER_CREATED);
        order.setPrice(price);
        order.setNewEntity(isNew);
        return order;
    }
}
