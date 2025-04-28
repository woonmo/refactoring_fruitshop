package com.spring.refruitshop.repository.order;

import com.spring.refruitshop.domain.order.OrderItem;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    @Query("SELECT DISTINCT oi FROM OrderItem oi JOIN FETCH oi.product WHERE oi.order.orderNo = :orderNo")
    List<OrderItem> findAllByOrderNo(@NotNull @Param("orderNo") Long orderNo);



    @Query("SELECT oi.shipStatus, COUNT(oi) FROM OrderItem oi JOIN Order o ON oi.order.orderNo = o.orderNo WHERE o.user.no = :userNo GROUP BY oi.shipStatus "+
            " ORDER BY CASE oi.shipStatus "+
            " WHEN 'PREPARING' THEN 1 " +
            " WHEN 'SHIPPING' THEN 2 " +
            " WHEN 'DELIVERED' THEN 3 " +
            " ELSE 4 " +
            "END ")
    List<Object[]> findShipStatusCounts(@Param("userNo") Long no);     // 회원 번호로 주문상품 상태를 반환
}
