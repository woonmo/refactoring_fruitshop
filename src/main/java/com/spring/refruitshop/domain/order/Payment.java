package com.spring.refruitshop.domain.order;

import com.spring.refruitshop.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment {

    /**
     * 결제 Entity 클래스
     */

    @Id
    @Column(nullable = false, unique = true, name = "pay_no")
    @SequenceGenerator(name = "SEQ_PAYMENTS_GENERATOR", sequenceName = "payments_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PAYMENTS_GENERATOR")
    private Long no;

    @ManyToOne
    @JoinColumn(name = "fk_order_no", referencedColumnName = "order_no", insertable = false, updatable = false)
    private Order order;    // 주문번호

    @ManyToOne
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no", insertable = false, updatable = false)
    private User user;  // 회원

    private LocalDateTime payDate;  // 결제일자

    @PrePersist
    public void prePersist() {
        this.payDate = this.payDate == null ? LocalDateTime.now() : this.payDate;
    }

    @Column(columnDefinition = "NUMBER DEFAULT 0")
    private int payRefund;  // 환불여부(1: 환불, 0: 미환불)
}
