package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateOrderStatusRequest {

    // 주문 상태 변경 요청 DTO
    private String orderCode;   // 주문번호
    private String orderStatus; // 주문 상태

}
