package com.cbtl.orderservice.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import event.inventory.InventoryStatus;
import event.order.OrderStatus;
import event.payment.PaymentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("orders")
public class Order implements Persistable<UUID> {
    @Id
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Integer price;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private InventoryStatus inventoryStatus;

    @Transient
    @JsonIgnore
    private boolean newEntity;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return newEntity;
    }
}
