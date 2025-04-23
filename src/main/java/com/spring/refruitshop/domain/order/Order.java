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
    @Column(nullable = false, unique = true, name = "order_no", updatable = false)
    @SequenceGenerator(name = "SEQ_ORDER_GENERATOR", sequenceName = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_GENERATOR")
    @NotNull
    private Long orderNo;   // 주문번호 (20250412-1 형식)

    @ManyToOne
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no")
    @NotNull
    private User user;   // 회원

    @CreatedDate
    @Column(name = "order_date", updatable = false)
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

    @Column(name = "order_payprice")
    private int paymentPrice;   // 결제 금액

    @Column(name = "order_code", unique = true)
    private String orderCode;   // 포매팅 된 주문번호 20250422110716-1 형태

    @Column(name = "order_receiver")
    private String receiverName;    // 받는 사람

    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "receive_zipcode")),
            @AttributeOverride(name = "address", column = @Column(name = "receive_address")),
            @AttributeOverride(name = "detailAddress", column = @Column(name = "receive_detailaddress")),
            @AttributeOverride(name = "extraAddress", column = @Column(name = "receive_extraaddress"))
    })
    @Embedded
    private Address receiveAddress;    // 배송지  주소


    @OneToMany(mappedBy = "order", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    // mappedBy: 양방향 연관관계에서 주인이 아님을 표현(orderItem 에서 외래키를 관리)
    // fetch: 지연 로딩 방식 사용 Order 엔티티를 조회할 때 orderItems는 즉시 로딩되지 않고 orderItems 를 조회할 때만 데이터베이스에 접근 -> 성능 최적화
    // cascade: Order 엔티티의 모든 영속성 작업이 연관된 OrderItem 에도 자동으로 적용(Order 엔티티를 저장하면 연결된 모든 orderItem도 함께 저장, 삭제도 마찬가지)
    // orphanRemoval: Order 엔티티에서 orderItems 컬렉션에서 특정 orderItem 엔티티를 제거하면 그 엔티티는 데이터베이스에서도 삭제된다
    // 부모 자식 관계에서 연결이 끊어진 객체를 자동으로 제거해해줌(주문내역 일부 삭제 등에 사용)
    // 상품-장바구니 처럼 하위 항목을 가지는 경우가 아니라면 cascade = CascadeType.ALL, orphanRemoval = true 속성은 적절하지 않음
    @Builder.Default
    private List<OrderItem> orderItems = new ArrayList<>();


    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void initializeOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    // 주문번호를 (20250412-1) 형식으로 만들어주는 메소드
    public String getFormattedOrderNo() {
        if (orderDate == null) {
            throw new IllegalStateException("주문일자가 설정되지 않았습니다.");
        }
        String datePart = orderDate.format(DateTimeFormatter.ofPattern("yyyyMMddhhmmss"));
        return datePart + "-" + orderNo;
    }
}
