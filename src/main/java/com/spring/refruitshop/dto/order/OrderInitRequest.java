package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderInitRequest {

    // 상품 상세 페이지에서 주문 요청 시

    // null 사용을 위한 wrapper 클래스로 받음
    private Long productNo;     // 상품번호
    private Integer quantity;   // 상품수량

    // 장바구니 페이지에서 주문 요청 시
    private List<CartItemRequest> cartItemList;    // 장바구니에 담긴 상품정보
}
