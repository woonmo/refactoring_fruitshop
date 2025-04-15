package com.spring.refruitshop.controller.product.dto;

import com.spring.refruitshop.domain.product.ProductSeasons;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProductSearchRequest {

    public static final String SORT_FIELD_JPQL = "no";
    public static final String SORT_FIELD_NATIVE = "prod_no";

    private String product; // 상품명
    private String season;  // 계절명

    private int page = 1;       // 현재 페이지 번호
    private int size = 16;      // 페이지당 보여줄 개수

    public void setProduct(String product) {
        this.product = product == null ? "" : product.trim().toUpperCase();
    }

    public void setSeason(String season) {
        if (season == null
            || !ProductSeasons.SPRING.toString().equalsIgnoreCase(season)
            && !ProductSeasons.SUMMER.toString().equalsIgnoreCase(season)
            && !ProductSeasons.AUTUMN.toString().equalsIgnoreCase(season)
            && !ProductSeasons.WINTER.toString().equalsIgnoreCase(season)) {

            season = "";
        }
        this.season = season;
    }

    public void setPage(int page) {
        this.page = page < 0 ? 1 : page;
    }

    public void setSize(int size) {
        this.size = size < 0 ? 10 : size;
    }


    // 페이징 객체 생성
    public Pageable toPageable(String sort) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(Sort.Direction.DESC, sort));
    }


    @Override
    public String toString() {
        return "ProductSearchRequest{" +
                "product='" + product + '\'' +
                ", season='" + season + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
