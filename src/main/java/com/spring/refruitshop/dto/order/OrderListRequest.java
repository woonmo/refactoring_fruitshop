package com.spring.refruitshop.dto.order;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class OrderListRequest {

    // 주문 검색 조건 요청 DTO

    public static final String SORT_FIELD_JPQL = "orderNo";
    public static final String SORT_FIELD_NATIVE = "order_no";

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate fromDate = LocalDate.now().plusMonths(-3);    // 시작일
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate  = LocalDate.now();       // 종료일
    private String orderCode   = "";                    // 주문코드
    private String orderStatus = "";                    // 주문상태

    private int page = 1;       // 현재 페이지 번호
    private int size = 5;       // 페이지당 보여줄 개수


    // 페이징 객체 생성
    public Pageable toPageable(String sort) {
        return PageRequest.of(this.page - 1, this.size, Sort.by(Sort.Direction.DESC, sort));
    }

    @Override
    public String toString() {
        return "OrderListRequest{" +
                "fromDate='" + fromDate + '\'' +
                ", endDate='" + endDate + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", orderStatus='" + orderStatus + '\'' +
                ", page=" + page +
                ", size=" + size +
                '}';
    }
}
