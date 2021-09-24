package com.cbtl.orderservice.controller;

import com.cbtl.orderservice.entity.Order;
import com.cbtl.orderservice.error.PriceClientException;
import com.cbtl.orderservice.service.CommandService;
import com.cbtl.orderservice.service.OrderService;
import dto.OrderRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {
    public static final Logger logger = LoggerFactory.getLogger(OrderController.class);
    private final CommandService commandService;
    private final OrderService orderService;

    @Autowired
    public OrderController(CommandService commandService, OrderService orderService) {
        this.commandService = commandService;
        this.orderService = orderService;
    }

    @PostMapping("/create")
    public Mono<ResponseEntity<Order>> createOrder(@RequestBody OrderRequestDto requestDto) {
        logger.info("Received order" + requestDto);
        requestDto.setOrderId(UUID.randomUUID());
        return commandService.createOrder(requestDto)
                .map(order -> ResponseEntity.ok().body(order))
                .onErrorResume((error) -> {
                    if (error instanceof PriceClientException pce) {
                        logger.error("Price Client error " + pce.getResponseStatus());
                        if (pce.getResponseStatus() == HttpStatus.NOT_FOUND.value()) {
                            return Mono.just(ResponseEntity.badRequest().body(null));
                        }
                        return Mono.just(ResponseEntity.status(pce.getResponseStatus()).body(null));
                    }
                    logger.error("Error", error);
                    return Mono.just(ResponseEntity.internalServerError().body(null));
                });
    }

    @GetMapping("all")
    public Flux<Order> getAllOrders() {
        logger.info("Getting all orders");
        return orderService.getAllOrders();
    }
}
