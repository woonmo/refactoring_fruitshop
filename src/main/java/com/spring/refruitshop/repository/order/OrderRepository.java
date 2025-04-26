package com.spring.refruitshop.repository.order;

import com.spring.refruitshop.domain.order.Order;
import com.spring.refruitshop.dto.order.OrderListResponse;
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
//    @Query(
//            value = "SELECT * FROM orders o JOIN order_items oi  ON o.order_no = oi.fk_order_no WHERE (TO_CHAR(o.order_date, 'yyyy-mm-dd') BETWEEN :fromDate AND :endDate) AND UPPER(o.order_code) LIKE '%'|| :orderCode || '%'",
//            countQuery = "SELECT COUNT(*) FROM orders o WHERE (TO_CHAR(o.order_date, 'yyyy-mm-dd') BETWEEN :fromDate AND :endDate) AND UPPER(o.order_code) LIKE '%'|| :orderCode || '%'",
//            nativeQuery = true
//    )
    @Query("SELECT DISTINCT o FROM Order o JOIN FETCH o.orderItems oi JOIN FETCH oi.product JOIN FETCH o.user WHERE o.user.no = :userNo AND CAST(o.orderStatus AS string) LIKE :orderStatus AND o.orderDate BETWEEN :fromDate AND :endDate AND o.orderCode LIKE :orderCode")
    Page<Order> findByOrderDateAndOrderStatusAndOrderCodeContaining(@Param("userNo") Long userNo, @Param("fromDate") LocalDateTime fromDate, @Param("endDate") LocalDateTime endDate, @Param("orderCode") String searchOrderCode, @Param("orderStatus") String searchOrderStatus, Pageable pageable);
}
