package com.spring.refruitshop.util;

import org.springframework.data.domain.Page;

public class PagingUtil {

    public static Pagination getPagination(Page<?> page, int blockSize) {
        // 블럭 개수와 페이지 객체를 받아 페이지 정보 제작
        int currentPage = page.getNumber() + 1;     // 현재 페이지 번호
        int totalPages = page.getTotalPages();      // 총 페이지 수

        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;    // 시작 페이지 번호
        int endPage = Math.min(startPage + blockSize -1, totalPages);       // 끝 페이지 번호

        boolean hasPrev = startPage > 1;            // 이전 버튼 활성화 여부
        boolean hasNext = endPage < totalPages;     // 다음 버튼 활성화 여부

        return new Pagination(blockSize, startPage, endPage, currentPage, totalPages, hasPrev, hasNext);
    }
}
