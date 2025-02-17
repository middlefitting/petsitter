package com.kt.petsitter.repository.order;

import com.kt.petsitter.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
