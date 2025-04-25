package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.domain.order.Order;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
@Setter
public class OrderDetailResponse {

    // 주문 상세페이지 조회시 응답 DTO


    // 주문 정보
    private Long orderNo;           // 주문번호
    private String orderCode;       // 주문 코드
    private String orderDate;       // 주문 일시
    private String orderStatus;     // 주문 상태


    // 상품 정보
    List<OrderDetailItem> items;    // 주문상품 정보

    // 배송 정보
    private String receiverName;    // 수령인
    private String receiverTel;     // 수령인 연락처
    private String zipCode;         // 우편번호
    private String address;         // 주소
    private String detailAddress;   // 상세주소
    private String extraAddress;    // 참고사항


    // 결제 정보
    private Long totalPrice;        // 주문총액
    private int discount;           // 할인액
    private int paymentPrice;       // 결제금액


    public OrderDetailResponse(Order order, List<OrderDetailItem> items) {
        this.orderNo = order.getOrderNo();
        this.orderCode = order.getOrderCode();
        this.orderDate = order.getOrderDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.orderStatus = order.getOrderStatus().toString();

        this.items = items;

        this.receiverName = order.getReceiverName();
        this.receiverTel = order.getReceiverTel();
        this.zipCode = order.getReceiveAddress().getZipcode();
        this.address = order.getReceiveAddress().getAddress();
        this.detailAddress = order.getReceiveAddress().getDetailAddress();
        this.extraAddress = order.getReceiveAddress().getExtraAddress();


        this.totalPrice = order.getOrderTprice();
        this.discount = order.getOrderDiscount();
        this.paymentPrice = order.getPaymentPrice();
    }
}
