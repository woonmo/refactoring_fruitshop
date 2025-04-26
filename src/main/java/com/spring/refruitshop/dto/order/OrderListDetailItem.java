package com.spring.refruitshop.dto.order;

import com.spring.refruitshop.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderListDetailItem {

    private Long prodNo;        // 상품 번호
    private String prodName;    // 상품명
    private String thumbnail;   // 이미지 썸네일


    public OrderListDetailItem (Product product) {
        this.prodNo = product.getNo();
        this.prodName = product.getName();
        this.thumbnail = product.getThumbnail();
    }
}
