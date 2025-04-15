package com.spring.refruitshop.controller.product.dto;

import com.spring.refruitshop.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductSearchResponse {

    private Long no;            // 상품 번호
    private String name;        // 상품 명
    private int price;          // 상품가격
    private String thumbnail;   // 상품 썸네일
    private String season;      // 계절

    public ProductSearchResponse(Product product) {
        this.no = product.getNo();
        this.name = product.getName();
        this.price = product.getPrice();
        this.thumbnail = product.getThumbnail();
        this.season = product.getSeason().toString();
    }

    @Override
    public String toString() {
        return "ProductSearchResponse{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", thumbnail='" + thumbnail + '\'' +
                ", season='" + season + '\'' +
                '}';
    }
}
