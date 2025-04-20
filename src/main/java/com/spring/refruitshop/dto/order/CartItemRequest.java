package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {

    // 장바구니에 담긴 상품 정보 DTO
    private Long productNo;
    private Integer quantity;
}
