package com.cbtl.inventoryservice.controller;

import com.cbtl.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

@RestController
@RequestMapping("price")
public class PriceController {
    private final InventoryService inventoryService;

    @Autowired
    public PriceController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/:productId")
    public Mono<Integer> getPrice(@RequestParam("productId") UUID productId) {
        return inventoryService.getPrice(productId);
    }
}
