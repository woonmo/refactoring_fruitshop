package com.spring.refruitshop.dto.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class MyPageShipItem {

    private String status;   // 배송상태명
    private Integer quantity;    // 개수
}
