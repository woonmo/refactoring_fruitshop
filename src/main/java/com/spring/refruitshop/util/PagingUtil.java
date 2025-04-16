package com.spring.refruitshop.util;

import org.springframework.data.domain.Page;

public class PagingUtil {

    public static Pagination getPagination(Page<?> page, int blockSize) {
//        // 블럭 개수와 페이지 객체를 받아 페이지 정보 제작
//        int currentPage = page.getNumber() + 1;     // 현재 페이지 번호
//        int totalPages = page.getTotalPages();      // 총 페이지 수
//
//        int startPage = ((currentPage - 1) / blockSize) * blockSize + 1;    // 시작 페이지 번호
//        int endPage = Math.min(startPage + blockSize -1, totalPages);       // 끝 페이지 번호
//
//        boolean hasPrev = startPage > 1;            // 이전 버튼 활성화 여부
//        boolean hasNext = endPage < totalPages;     // 다음 버튼 활성화 여부



        // 항상 현재페이지가 가운데 오도록 하는 페이징바 제작
        int currentPage = page.getNumber() + 1;      // 현재 페이지 번호
        int totalPages = page.getTotalPages();

        int startPage;
        int endPage;
        int halfBlock = blockSize / 2;

        if (totalPages <= blockSize) {
            startPage = 1;
            endPage = totalPages;
        } else if (currentPage <= halfBlock) {
            startPage = 1;
            endPage = blockSize;
        } else if (currentPage >= totalPages - halfBlock) {
            startPage = totalPages - blockSize + 1;
            endPage = totalPages;
        } else {
            startPage = currentPage - halfBlock;
            endPage = currentPage + halfBlock;
            if (blockSize % 2 == 0) endPage--;
        }

        boolean hasPrev = startPage > 1;
        boolean hasNext = endPage < totalPages;

        return new Pagination(blockSize, startPage, endPage, currentPage, totalPages, hasPrev, hasNext);
    }
}
