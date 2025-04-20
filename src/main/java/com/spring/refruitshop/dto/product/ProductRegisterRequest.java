package com.spring.refruitshop.dto.product;

import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.product.ProductSeasons;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductRegisterRequest {

    @NotNull(message = "회원정보는 필수입니다 !!")
    private Long userNo;    // 회원번호 (관리자 검증용)

    @NotBlank(message = "상품명은 필수입력 사항입니다 !!")
    private String name;    // 상품명

    @NotNull(message = "상품 원가는 필수입력 사항입니다 !!")
    @Min(value = 0, message = "상품 원가는 0 이상이어야 합니다 !!")
    private int cost;       // 상품원가

    @NotNull(message = "상품 판매가는 필수입력 사항입니다 !!")
    @Min(value = 0, message = "상품 판매가는 0 이상이어야 합니다 !!")
    private int price;      // 상품판매가격

    @NotNull(message = "상품 재고량은 필수입력 사항입니다 !!")
    @Min(value = 0, message = "상품 재고량은 0 이상이어야 합니다!!")
    private int inventory;  // 상품 재고량


    private String thumbnail;   // 상품 썸네일
    private String description; // 상품 설명
    private String season;      // 계절


    public Product toEntity() {
        return Product.builder()
                .name(name)
                .cost(cost)
                .price(price)
                .inventory(inventory)
                .thumbnail(thumbnail)
                .description(description)
                .season(ProductSeasons.valueOf(season))
                .build();
    }
}
