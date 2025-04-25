package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListRequest {

    // 주문 검색 조건 요청 DTO

    private String fromDate;    // 시작일
    private String endDate;     // 종료일
    private String orderCode;   // 주문코드
    private String orderStatus; // 주문상태

}
