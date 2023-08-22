package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
