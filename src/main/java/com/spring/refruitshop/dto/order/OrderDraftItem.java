package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderDraftItem {

    // 주문 상품에 대한 정보

    private Long no;            // 상품번호
    private String name;        // 상품명
    private int price;          // 상품가격
    private int quantity;       // 상품수량
    private String thumbnail;   // 상품이미지



    public OrderDraftItem(Product product, int quantity) {
        this.no = product.getNo();
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnail = product.getThumbnail();
        this.quantity = quantity;
    }
}
