package com.spring.refruitshop.controller.cart.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddItemRequest {

    @NotNull(message = "상품 정보는 필수입니다 !!")
    private Long productNo;     // 상품번호

    @Min(value = 1, message = "상품 수량은 1 이상이어야 합니다 !!")
    private int quantity;       // 상품수량
}
