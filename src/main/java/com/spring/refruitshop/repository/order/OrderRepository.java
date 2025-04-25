package com.spring.refruitshop.repository.order;

import com.spring.refruitshop.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderCode = :orderCode AND o.user.no = :userNo")
    Optional<Order> findByOrderCodeAndUserNo(@Param("orderCode") String orderCode, @Param("userNo") Long userNo);
}
