package com.spring.refruitshop.controller.cart.dto;

import com.spring.refruitshop.domain.cart.Cart;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddItemResponse {

    // 장바구니 등록 시 등록 정보를 보여줄 DTO
    private Long userNo;            // 회원번호
    private String userId;          // 회원 아이디
    private String userName;        // 회원명
    private String productName;     // 상품명
    private int price;              // 상품가격
    private int quantity;           // 상품수량
    private int totalPrice;         // 상품 총 가격

    public AddItemResponse(Cart cart) {
        this.userNo = cart.getUser().getNo();
        this.userId = cart.getUser().getUserId();
        this.userName = cart.getUser().getName();
        this.productName = cart.getProduct().getName();
        this.price = cart.getProduct().getPrice();
        this.quantity = cart.getQuantity();
        this.totalPrice = cart.sumPrice(this.price, this.quantity);
    }
}
