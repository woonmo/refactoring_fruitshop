package com.spring.refruitshop.dto.product;

import com.spring.refruitshop.domain.product.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDetailResponse {

    // 상품 상세정보 반환 DTO

    private Long no;            // 상품 번호
    private String name;        // 상품명
    private int price;          // 판매가격
    private int inventory;      // 재고
    private String thumbnail;   // 상품 썸네일
    private String description; // 상품 설명


    public ProductDetailResponse(Product product) {
        this.no = product.getNo();
        this.name = product.getName();
        this.thumbnail = product.getThumbnail();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.inventory = product.getInventory();
    }


    @Override
    public String toString() {
        return "ProductDetailResponse{" +
                "no=" + no +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", inventory=" + inventory +
                ", thumbnail='" + thumbnail + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
