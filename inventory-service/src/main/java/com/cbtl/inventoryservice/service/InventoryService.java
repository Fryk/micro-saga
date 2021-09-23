package com.cbtl.inventoryservice.service;

import com.cbtl.inventoryservice.entity.Inventory;
import com.cbtl.inventoryservice.repository.InventoryRepository;
import dto.InventoryDto;
import event.inventory.InventoryEvent;
import event.inventory.InventoryStatus;
import event.order.OrderEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    public Mono<Integer> getPrice(UUID productId) {
        return inventoryRepository.findById(productId).map(Inventory::getPrice);
    }

    @Transactional
    public Mono<InventoryEvent> newOrder(OrderEvent orderEvent) {
        InventoryDto inventoryDto = InventoryDto.of(orderEvent.getOrder().getOrderId(), orderEvent.getOrder().getProductId());
        return inventoryRepository.findById(orderEvent.getOrder().getProductId())
                .map(i -> new InventoryEvent(inventoryDto, InventoryStatus.RESERVED))
                .defaultIfEmpty(new InventoryEvent(inventoryDto, InventoryStatus.REJECTED));
    }

    @Transactional
    public void cancelOrder(OrderEvent orderEvent) {
        // nothing
    }
}
