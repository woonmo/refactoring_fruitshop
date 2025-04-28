package com.spring.refruitshop.domain.order;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ShipStatus {
    PREPARING("배송준비중"), SHIPPING("배송중"), DELIVERED("배송완료");

    private final String description;
}
