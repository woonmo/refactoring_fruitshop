package com.spring.refruitshop.controller.cart.dto;

import com.spring.refruitshop.domain.cart.Cart;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {

    private Long cartNo;            // 장바구니번호
    private Long userNo;            // 회원번호
    private String userId;          // 회원 아이디
    private String userName;        // 회원명
    private String productName;     // 상품명
    private String productThumbnail; // 상품 이미지
    private Long productNo;         // 상품 번호
    private int productInventory;   // 상품 재고
    private int price;              // 상품가격
    private int quantity;           // 상품수량
    private int totalPrice;         // 상품 총 가격

    public CartItemResponse(Cart cart) {
        this.cartNo = cart.getCartNo();
        this.userNo = cart.getUser().getNo();
        this.userId = cart.getUser().getUserId();
        this.userName = cart.getUser().getName();
        this.productName = cart.getProduct().getName();
        this.productThumbnail = cart.getProduct().getThumbnail();
        this.productNo = cart.getProduct().getNo();
        this.productInventory = cart.getProduct().getInventory();
        this.price = cart.getProduct().getPrice();
        this.quantity = cart.getQuantity();
        this.totalPrice = cart.sumPrice(price, quantity);
    }

}
