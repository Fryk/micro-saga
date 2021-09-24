package com.cbtl.inventoryservice.controller;

import com.cbtl.inventoryservice.service.InventoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("price")
public class PriceController {
    public static final Logger logger = LoggerFactory.getLogger(PriceController.class);
    private final InventoryService inventoryService;

    @Autowired
    public PriceController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/{productId}")
    public Mono<Integer> getPrice(@PathVariable("productId") UUID productId) {
        logger.info("Getting price for " + productId);
        return inventoryService.getPrice(productId);
    }
}
