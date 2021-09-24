package com.cbtl.inventoryservice.repository;

import com.cbtl.inventoryservice.entity.ReservedInventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import java.util.UUID;

public interface ReservedInventoryRepository extends ReactiveCrudRepository<ReservedInventory, UUID> {
}
