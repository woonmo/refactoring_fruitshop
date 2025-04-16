package com.spring.refruitshop.controller.product;

import com.spring.refruitshop.controller.product.dto.ProductSearchRequest;
import com.spring.refruitshop.controller.product.dto.ProductSearchResponse;
import com.spring.refruitshop.service.product.ProductService;
import com.spring.refruitshop.util.Pagination;
import com.spring.refruitshop.util.PagingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/products")
@Slf4j
public class ProductViewController {

    private final ProductService productService;

    public ProductViewController(ProductService productService) {
        this.productService = productService;
    }

    // 전체 상품 목록 조회
    @GetMapping
    public String viewProducts(Model model, ProductSearchRequest request) {
        Page<ProductSearchResponse> productPage = productService.pagingProductList(request);      // 페이징 처리 된 상품리스트

        Pagination pagination = PagingUtil.getPagination(productPage, 5);   // 페이징 정보 생성

        model.addAttribute("productList", productPage.getContent());    // 상품 조회 내용
        model.addAttribute("pagination", pagination);                   // 페이징 정보
        model.addAttribute("product", request.getProduct());            // 검색어

//        log.info("전체상품조회 검색어: {}", request.getProduct());
//        log.info("전체상품조회 생성된 페이지바 정보: {}", pagination);

        return "product/productList";
    }// end of public String viewProducts(Model model) ----------------------


    // 계절 상품 목록 조회
    @GetMapping("/{season}")
    public String viewProductsSeason(Model model, @PathVariable("season") String season, ProductSearchRequest request) {
        request.setSeason(season);  // 검색한 계절
        Page<ProductSearchResponse> productPage = productService.pagingProductListBySeason(request);

        Pagination pagination = PagingUtil.getPagination(productPage, 5);   // 페이징 정보 생성

        model.addAttribute("productList", productPage.getContent());    // 상품 조회 내용
        model.addAttribute("pagination", pagination);                   // 페이징 정보
        model.addAttribute("product", request.getProduct());            // 검색어

//        log.info("계절상품조회 검색어: {}", request.getProduct());
//        log.info("계절상품조회 생성된 페이지바 정보: {}", pagination);

        return "product/productList";
    }// end of public String viewProductsSeason(Model model, @PathVariable("season") String season, ProductSearchRequest request) -----------------------



}
