package com.cbtl.orderservice.repository;

import com.cbtl.orderservice.entity.Order;
import event.inventory.InventoryStatus;
import event.payment.PaymentStatus;
import org.springframework.data.r2dbc.repository.Modifying;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
    @Modifying
    @Query("UPDATE orders SET payment_status = :paymentStatus WHERE id = :orderId")
    Mono<Void> setPaymentStatus(PaymentStatus paymentStatus, UUID orderId);

    @Modifying
    @Query("UPDATE orders SET inventory_status = :inventoryStatus WHERE id = :orderId")
    Mono<Void> setInventoryStatus(InventoryStatus inventoryStatus, UUID orderId);
}
