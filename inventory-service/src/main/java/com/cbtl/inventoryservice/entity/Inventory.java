package com.cbtl.inventoryservice.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("inventory")
public class Inventory {
    @Id
    private UUID id;
    private String name;
    private Integer price;
}
