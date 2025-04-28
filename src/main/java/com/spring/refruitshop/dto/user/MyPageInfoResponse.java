package com.spring.refruitshop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class MyPageInfoResponse {

    // 마이페이지에 표시할 정보 응답 DTO
    // 배송정보 Map, 쿠폰정보 List

    // 상품 배송정보
    private List<MyPageShipItem> statusList;

    // 쿠폰(추후 추가)
    private List<Object> couponList;

}
