package com.spring.refruitshop.repository.order;

import com.spring.refruitshop.domain.order.Order;
import com.spring.refruitshop.domain.order.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o WHERE o.orderCode = :orderCode AND o.user.no = :userNo")
    Optional<Order> findByOrderCodeAndUserNo(@Param("orderCode") String orderCode, @Param("userNo") Long userNo);

    // 주문코드 포함 주문 조회
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product JOIN FETCH o.user " + "WHERE o.user.no = :userNo " + "AND o.orderDate BETWEEN :fromDate AND :endDate " + "AND (:searchOrderCode IS NULL OR o.orderCode LIKE :searchOrderCode) " + "AND (:searchOrderStatus IS NULL OR o.orderStatus = :searchOrderStatus)")
    Page<Order> findByOrderDateAndOrderStatusAndOrderCodeContaining( @Param("userNo") Long userNo, @Param("fromDate") LocalDateTime fromDate, @Param("endDate") LocalDateTime endDate, @Param("searchOrderCode") String searchOrderCode, @Param("searchOrderStatus") OrderStatus searchOrderStatus, Pageable pageable);

}
