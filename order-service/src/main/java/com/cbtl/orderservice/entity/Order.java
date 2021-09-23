package com.cbtl.orderservice.entity;

import event.inventory.InventoryStatus;
import event.order.OrderStatus;
import event.payment.PaymentStatus;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@Data
@Table("orders")
public class Order {
    @Id
    private UUID id;
    private UUID userId;
    private UUID productId;
    private Integer price;
    private OrderStatus orderStatus;
    private PaymentStatus paymentStatus;
    private InventoryStatus inventoryStatus;

}
