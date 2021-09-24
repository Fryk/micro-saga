package com.cbtl.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@AllArgsConstructor(staticName = "of")
@Table("reserved_inventory")
public class ReservedInventory implements Persistable<UUID> {
    @Id
    private UUID orderId;
    private UUID productId;
    private Integer quantity;

    @Override
    public UUID getId() {
        return getOrderId();
    }

    @Transient
    @JsonIgnore
    private boolean newEntity;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return newEntity;
    }
}
