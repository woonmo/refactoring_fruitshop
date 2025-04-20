package com.spring.refruitshop.dto.product;

import com.spring.refruitshop.domain.product.Product;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductRegisterResponse {

    private Long prodNo;        // 상품 번호
    private String name;        // 상품명
    private String thumbnail;   // 상품 썸네일
    private String description; // 상품 설명
    private int price;          // 상품 가격
    private int cost;           // 상품 원가
    private int inventory;       // 상품 재고
    private String createAt;    // 상품 등록일자
    private String modifiedAt;  // 상품 수정시간
    private String season;      // 계절

    public ProductRegisterResponse(Product product) {
        this.prodNo = product.getNo();
        this.name = product.getName();
        this.thumbnail = product.getThumbnail();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.cost = product.getCost();
        this.inventory = product.getInventory();
        this.season = product.getSeason().toString();
        this.createAt = product.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.modifiedAt = product.getUpdateAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
