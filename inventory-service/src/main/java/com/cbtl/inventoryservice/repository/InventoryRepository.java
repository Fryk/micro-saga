package com.cbtl.inventoryservice.repository;

import com.cbtl.inventoryservice.entity.Inventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface InventoryRepository extends ReactiveCrudRepository<Inventory, UUID> {
}
