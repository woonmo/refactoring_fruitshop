package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.domain.order.OrderItem;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class OrderDetailItem {

    // 주문 상세 페이지에 들어가는 상품 1개의 정보


    private Long prodNo;            // 상품번호
    private String prodName;        // 상품명
    private int price;              // 가격
    private int quantity;           // 수량
    private String thumbnail;       // 상품이미지
    private String deliveryDate;    // 배송일
    private String shipStatus;      // 배송상태


    public OrderDetailItem (OrderItem orderItem) {
        this.prodNo = orderItem.getProduct().getNo();
        this.prodName = orderItem.getProduct().getName();
        this.price = orderItem.getPrice();
        this.quantity = orderItem.getQuantity();
        this.thumbnail = orderItem.getProduct().getThumbnail();
        this.deliveryDate = orderItem.getDeliveryDate() == null ? "": orderItem.getDeliveryDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.shipStatus = orderItem.getShipStatus().toString();
    }

    @Override
    public String toString() {
        return "OrderDetailItem{" +
                "prodNo=" + prodNo +
                ", prodName='" + prodName + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", thumbnail='" + thumbnail + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", shipStatus='" + shipStatus + '\'' +
                '}';
    }
}
