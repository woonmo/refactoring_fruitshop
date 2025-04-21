package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderConfirmRequest {

    // 1. 배송정보
    private String receiverName;    // 받는이
    private String receiverTel;     // 받는이 연락처
    private String zipCode;         // 우편번호
    private String address;         // 주소
    private String detailAddress;   // 상세주소
    private String extraAddress;    // 참고사항

    // 2. 가격정보 (프론트에서 진행)
    private int totalPrice;     // 주문총액
    private int discount;       // 할인액
    private int finalPrice;     // 최종결제예정금액
    private int point;          // 적립금

    // 3. 배송요청사항
    private String requestNote;

    // 4. 기본 배송지 설정 여부
    private String isDefaultShip;

}

