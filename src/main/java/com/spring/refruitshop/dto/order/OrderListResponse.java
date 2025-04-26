package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.util.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponse {

    // 가공한 주문 목록 정보를 반환하는 DTO

    // 주문 정보
    private List<OrderListItem> orderList;

    // 페이징 정보
    private Pagination pagination;

    public OrderListResponse (List<OrderListItem> orderList, Pagination pagination) {
        this.orderList = orderList;
        this.pagination = pagination;
    }
}
