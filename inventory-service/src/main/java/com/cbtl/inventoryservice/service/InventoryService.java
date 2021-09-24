package com.cbtl.inventoryservice.service;

import com.cbtl.inventoryservice.entity.Inventory;
import com.cbtl.inventoryservice.entity.ReservedInventory;
import com.cbtl.inventoryservice.repository.InventoryRepository;
import com.cbtl.inventoryservice.repository.ReservedInventoryRepository;
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
    private final ReservedInventoryRepository reservedInventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository, ReservedInventoryRepository reservedInventoryRepository) {
        this.inventoryRepository = inventoryRepository;
        this.reservedInventoryRepository = reservedInventoryRepository;
    }

    public Mono<Integer> getPrice(UUID productId) {
        return inventoryRepository.findById(productId).map(Inventory::getPrice);
    }

    @Transactional
    public Mono<InventoryEvent> newOrder(OrderEvent orderEvent) {
        InventoryDto inventoryDto = InventoryDto.of(orderEvent.getOrder().getOrderId(), orderEvent.getOrder().getProductId());
        return inventoryRepository.findById(orderEvent.getOrder().getProductId())
                .filter(i -> i.getAmount() > 0)
                .flatMap(i -> {
                    i.setAmount(i.getAmount() - 1);
                    return inventoryRepository
                            .save(i)
                            .then(reservedInventoryRepository.save(createReservedInventory(orderEvent)))
                            .then(Mono.just(new InventoryEvent(inventoryDto, InventoryStatus.RESERVED)));
                })
                .defaultIfEmpty(new InventoryEvent(inventoryDto, InventoryStatus.REJECTED));
    }

    @Transactional
    public Mono<InventoryEvent> cancelOrder(OrderEvent orderEvent) {
        return Mono.zip(
                reservedInventoryRepository.findById(orderEvent.getOrder().getOrderId()),
                inventoryRepository.findById(orderEvent.getOrder().getProductId())
        ).flatMap(tuple -> {
            tuple.getT2().setAmount(tuple.getT2().getAmount() + tuple.getT1().getQuantity());
            return inventoryRepository
                    .save(tuple.getT2())
                    .then(reservedInventoryRepository.delete(tuple.getT1()))
                    .then(Mono.empty());
        });
    }

    private ReservedInventory createReservedInventory(OrderEvent orderEvent) {
        ReservedInventory reservedInventory = new ReservedInventory();
        reservedInventory.setOrderId(orderEvent.getOrder().getOrderId());
        reservedInventory.setProductId(orderEvent.getOrder().getProductId());
        reservedInventory.setQuantity(1);
        reservedInventory.setNewEntity(true);
        return reservedInventory;
    }
}
