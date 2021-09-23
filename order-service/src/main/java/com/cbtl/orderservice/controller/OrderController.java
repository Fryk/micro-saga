package com.cbtl.orderservice.controller;

import com.cbtl.orderservice.entity.Order;
import com.cbtl.orderservice.service.CommandService;
import dto.OrderRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("order")
public class OrderController {
    private final CommandService commandService;

    @Autowired
    public OrderController(CommandService commandService) {
        this.commandService = commandService;
    }

    @PostMapping("/create")
    public Mono<Order> createOrder(@RequestBody OrderRequestDto requestDto) {
        requestDto.setOrderId(UUID.randomUUID());
        return commandService.createOrder(requestDto);
    }
}
