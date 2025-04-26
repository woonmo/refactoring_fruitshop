package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.domain.order.Order;
import com.spring.refruitshop.util.Pagination;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class OrderListItem {

    // 회원의 주문 목록 리스트 DTO


    // 주문 관련
    private Long orderNo;           // 주문 번호
    private String orderCode;       // 주문 코드 (2025..)
    private String orderDate;       // 주문 일자
    private String orderStatus;     // 주문 상태
    private int paymentPrice;       // 주문 금액

    // 상품 정보
    List<OrderListDetailItem> items;    // 상품 정보


    public OrderListItem (Order order) {
        this.orderNo = order.getOrderNo();
        this.orderCode = order.getOrderCode();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.orderStatus = order.getOrderStatus().toString();
        this.paymentPrice = order.getPaymentPrice();

        this.items = order.getOrderItems().stream().map(orderItem -> new OrderListDetailItem(orderItem.getProduct())).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return "OrderListResponse{" +
                "orderNo=" + orderNo +
                ", orderCode='" + orderCode + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", paymentPrice=" + paymentPrice +
                ", items=" + items +
                '}';
    }
}
