package com.spring.refruitshop.dto.order;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemRequest {

    // 장바구니에 담긴 상품 정보 DTO
    private Long productNo;
    private Integer quantity;
    
    @JsonProperty("isFromCart")     // JSON 직렬/역직렬화 중 fromCart 로 인식할 수 있으므로 대상 key를 명확히 지정
    private boolean isFromCart;

    @Override
    public String toString() {
        return "CartItemRequest{" +
                "productNo=" + productNo +
                ", quantity=" + quantity +
                ", isFromCart=" + isFromCart +
                '}';
    }
}
