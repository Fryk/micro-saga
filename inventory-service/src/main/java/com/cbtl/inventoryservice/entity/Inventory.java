package com.cbtl.inventoryservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("inventory")
public class Inventory implements Persistable<UUID> {
    @Id
    private UUID id;
    private String name;
    private Integer price;
    private Integer amount;

    @Transient
    @JsonIgnore
    private boolean newEntity;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return newEntity;
    }
}
