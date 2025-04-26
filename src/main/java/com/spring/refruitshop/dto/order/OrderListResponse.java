package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.util.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderListResponse {

    // 주문 정보
    private List<OrderListItem> orderList;

    // 페이징 정보
    private Pagination pagination;

    public OrderListResponse (List<OrderListItem> orderList, Pagination pagination) {
        this.orderList = orderList;
        this.pagination = pagination;
    }
}
