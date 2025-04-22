package com.spring.refruitshop.domain.cart;

import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cart {

    /**
     * 장바구니 Entity 클래스 파일
     */

    @Id
    @Column(name = "cart_no", nullable = false, updatable = false)
    @SequenceGenerator(name = "SEQ_CART_GENERATOR", sequenceName = "cart_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_GENERATOR")
    private Long cartNo;    // 장바구니 번호

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no", nullable = false)
    private User user;  // 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_prod_no", referencedColumnName = "prod_no", nullable = false)
    private Product product;    // 상품


    @Column(name = "cart_prodquantity")
    // 최소 1개 이상
    private int quantity; // 장바구니 수량


    @Builder
    public Cart(User user, Product product, int quantity) {
        this.user = user;
        this.product = product;
        this.quantity = quantity;
    }

    // 장바구니에 담긴 상품의 수량을 수정 해주는 메소드
    public void updateQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("상품 수량은 1 이상이어야 합니다 !!");
        }
        this.quantity = quantity;
    }// end of public void updateQuantity(int quantity) ---------------------

    // 장바구니에 담은 상품의 가격 합계를 구해주는 메소드
    public int sumPrice(int price, int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("상품 수량은 1 이상이어야 합니다 !!");
        }
        return price * quantity;
    }// end of public int sumPrice(int price, int quantity) ---------------------------


    @Override
    public String toString() {
        return "Cart{" +
                "cartNo=" + cartNo +
                ", userNo=" + user.getNo() +
                ", userId=" + user.getUserId() +
                ", userName=" + user.getName() +
                ", productNo=" + product.getNo() +
                ", productName=" + product.getName() +
                ", quantity=" + quantity +
                '}';
    }
}
