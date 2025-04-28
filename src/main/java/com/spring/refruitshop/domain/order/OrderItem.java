package com.spring.refruitshop.domain.order;

import com.spring.refruitshop.domain.product.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Builder
@Entity
@Table(name = "order_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class OrderItem {

    /**
     * 주문상세 Entity 클래스
     */

    @Id
    @Column(name = "orderitem_no", nullable = false, updatable = false)
    @SequenceGenerator(name = "SEQ_ORDER_DETAIL_GENERATOR", sequenceName = "orderitem_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_DETAIL_GENERATOR")
    private Long no;    // 주문상세번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_order_no", referencedColumnName = "order_no")
    @NotNull
    private Order order;    // 주문번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_prod_no", referencedColumnName = "prod_no")
    @NotNull
    private Product product;    // 상품

    @Column(name = "orderitem_quantity")
    @NotNull
    @Min(1)
    private int quantity;  // 상품 수량

    @Column(name = "orderitem_price")
    @NotNull
    @Min(0)
    private int price; // 상품 가격

    @Enumerated(EnumType.STRING)
    @Column(name = "ship_status")
    @Builder.Default
    private ShipStatus shipStatus = ShipStatus.PREPARING;     // 배송상태(PREPARING, SHIPPING, DELIVERED)

    @LastModifiedDate
    @Column(name = "delivery_date")
    @Builder.Default
    private LocalDateTime deliveryDate = null; // 배송일자


    public void setOrder(Order order) {
        this.order = order;
    }

    // 필수 항목을 생성하는 정적 팩토리 메소드
    public static OrderItem createOrderItem(Order order, Product product, int quantity, int price) {
        return OrderItem.builder()
                .order(order)
                .product(product)
                .quantity(quantity)
                .price(price)
                .build();
    }

}
