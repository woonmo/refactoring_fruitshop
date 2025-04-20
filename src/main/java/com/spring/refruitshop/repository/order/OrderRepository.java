package com.spring.refruitshop.repository.order;

import com.spring.refruitshop.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
