package com.spring.refruitshop.domain.product;

import com.spring.refruitshop.domain.cart.Cart;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Product {

    /**
     * 상품 Entity 클래스 파일
     */

    @Id
    @Column(unique = true, nullable = false, name = "prod_no")
    @SequenceGenerator(name = "SEQ_PRODUCT_GENERATOR", sequenceName = "prod_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRODUCT_GENERATOR")
    private Long no;    // 상품 번호

    @Column(name = "prod_name", nullable = false)
    private String name;    // 상품 명

    @Column(name = "prod_cost", nullable = false)
    private int cost;   // 상품 원가

    @Column(name = "prod_price", nullable = false)
    private int price;  // 상품가격

    @Column(name = "prod_inventory")
    @Builder.Default
    private int inventory = 0;  // 재고량

    @CreatedDate
    @Column(name = "prod_regidate", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();  // 등록일자

    @LastModifiedDate
    @Column(name = "prod_modidate")
    @Builder.Default
    private LocalDateTime updateAt = LocalDateTime.now();   // 수정일자

    @Column(name = "prod_thumbnail")
    private String thumbnail;   // 상품 썸네일

    @Column(name = "prod_desc")
    private String description;    // 상품 설명

    @Column(name = "prod_season")
    @Enumerated(EnumType.STRING)
    private ProductSeasons season;  // 계절

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts;

    @PrePersist
    protected void prePersist() {
        if (carts == null) {
            carts = new ArrayList<>();
        }
    }


    // 재고 감소 메소드 (주문 시)
    public void decreaseInventory(int quantity) {
        if (quantity <= 0) {
            // 주문량이 0 이하 일경우
            throw new IllegalArgumentException("수량은 1 이상이어야 합니다.");
        }
        if (this.inventory < quantity) {
            // 주문량이 재고량보다 많을 경우
            throw new IllegalArgumentException("재고가 부족합니다 현재 재고량: "+ this.inventory);
        }
        this.inventory -= quantity;
    }// end of public void decreaseInventory(int quantity) -----------------
}
