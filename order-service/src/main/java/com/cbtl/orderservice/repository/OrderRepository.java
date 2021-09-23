package com.cbtl.orderservice.repository;

import com.cbtl.orderservice.entity.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrderRepository extends ReactiveCrudRepository<Order, UUID> {
}
