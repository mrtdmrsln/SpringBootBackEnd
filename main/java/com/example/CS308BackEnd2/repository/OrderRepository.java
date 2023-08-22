package com.example.CS308BackEnd2.repository;

import com.example.CS308BackEnd2.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository <Order, Long>{
    List<Order> findAllByUserIdOrderByCreatedDateDesc(Long userId);

    List<Order> findAllByUserId(Long userId);

    List<Order> findAllByUserIdAndStatus(Long userId, Order.Status status);

    List<Order> findAllByStatus(Order.Status status);
}
