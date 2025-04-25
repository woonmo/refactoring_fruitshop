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
}
