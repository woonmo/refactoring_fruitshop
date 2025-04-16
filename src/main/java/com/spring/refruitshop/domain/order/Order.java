package com.spring.refruitshop.domain.order;

import com.spring.refruitshop.domain.common.Address;
import com.spring.refruitshop.domain.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Order {

    /**
     * 주문 Entity 클래스
     */

    @Id
    @Column(nullable = false, unique = true, name = "order_no")
    @SequenceGenerator(name = "SEQ_ORDER_GENERATOR", sequenceName = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_GENERATOR")
    @NotNull
    private Long orderNo;   // 주문번호 (20250412-1 형식)

    @ManyToOne
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no")
    @NotNull
    private User user;   // 회원

    @CreatedDate
    @Column(name = "order_date")
    private LocalDateTime orderDate;    // 주문일자

    @Column(name = "order_request")
    private String request;     // 요청사항

    @Column(name = "order_tprice", nullable = false)
    private Long orderTprice;   // 주문총액

    @Column(name = "order_status")
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;    // 주문상태 (PENDING, COMPLETED, CANCELLED)

    @LastModifiedDate
    @Column(name = "order_changedate")
    private LocalDateTime orderChangeDate;    // 주문수정일자

    @Column(name = "order_discount")
    @Builder.Default
    private int orderDiscount = 0;     // 할인액


    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "receive_zipcode")),
            @AttributeOverride(name = "address", column = @Column(name = "receive_address")),
            @AttributeOverride(name = "detail_address", column = @Column(name = "receive_detailaddress")),
            @AttributeOverride(name = "extra_address", column = @Column(name = "receive_extraaddress"))
    })
    @Embedded
    private Address receiveAddress;    // 배송지  주소


    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItem> orderItems;

    @PrePersist
    public void prePersist() {
        if (orderItems == null) {
            orderItems = new ArrayList<>();
        }
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    // 주문번호를 (20250412-1) 형식으로 만들어주는 메소드
    public String getFormattedOrderNo() {
        if (orderDate == null) {
            throw new IllegalStateException("주문일자가 설정되지 않았습니다.");
        }
        String datePart = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return datePart + "-" + orderNo;
    }
}
