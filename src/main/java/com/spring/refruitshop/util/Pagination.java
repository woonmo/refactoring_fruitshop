package com.spring.refruitshop.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Pagination {

    private int startPage;      // 시작 페이지 번호
    private int endPage;        // 끝 페이지 번호
    private int currentPage;    // 현재 페이지 번호
    private int totalPages;     // 총 페이지 수

    private boolean hasPrev;    // 이전 페이지 확인
    private boolean hasNext;    // 다음페이지 확인
}
