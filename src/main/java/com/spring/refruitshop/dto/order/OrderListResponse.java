package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponse {

    // 주문 목록 리스트 반환 DTO



    // 주문 관련
    private Long orderNo;           // 주문 번호
    private String orderCode;       // 주문 코드 (2025..)
    private String orderDate;       // 주문 일자
    private String orderStatus;     // 주문 상태
    private int paymentPrice;       // 주문 금액

    // 상품 정보
    List<OrderDetailItem> items;    // 상품 정보
}
