package com.spring.refruitshop.service.product;

import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.product.ProductSeasons;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.product.*;
import com.spring.refruitshop.repository.product.ProductRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }


    // 상품 등록 메소드
    @Transactional
    public ProductRegisterResponse save(ProductRegisterRequest request) {
        // 유저 검증
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 관리자 검증
        if (!"ADMIN".equalsIgnoreCase(user.getRole().toString())) {
            throw new IllegalArgumentException("관리자만 접근 가능합니다 !!");
        }

        // 상품 등록 검증 상품명, 판매가가 같으면 이미 등록한 상품이다.
        productRepository.findByNameAndPrice(request.getName(), request.getPrice())
                .ifPresent(existProduct -> {
                    throw new IllegalArgumentException("이미 등록한 상품입니다 !!");
                });

        // 상품 등록
        Product product = productRepository.save(request.toEntity());
        log.info("상품 등록 완료, 등록한 회원: {}, 등록된 상품: {}", user, product);

        // 객체 반환
        return new ProductRegisterResponse(product);
    }// end of public ProductRegisterResponse save(ProductRegisterRequest request) -----------------------------


    // 페이징 처리 된 상품리스트
    public Page<ProductSearchResponse> pagingProductList(ProductSearchRequest request) {

        log.info("전체 상품조회 요청 정보: {}", request);
        Pageable pageable;
        Page<ProductSearchResponse> page;   // 초기화
        String searchProduct = request.getProduct();

        // 검색 키워드가 있을 경우 와 없을 경우를 분기
        if (searchProduct != null && !searchProduct.isEmpty()) {
            log.info("전체 상품 조회 검색 키워드: {}", searchProduct);

            pageable = request.toPageable(ProductSearchRequest.SORT_FIELD_NATIVE);   // LIKE 연산자 사용 시 네이티브 쿼리 사용해야
            // 검색 키워드가 있을 경우
            page = productRepository.findByNameContaining(searchProduct, pageable)
                    .map(product -> new ProductSearchResponse(product)); // 검색 키워드와 페이징 처리로 조회
        }
        else {
            // 검색 키워드가 없을 경우
            log.info("전체 상품 조회");
            pageable = request.toPageable(ProductSearchRequest.SORT_FIELD_JPQL);
            page = productRepository.findAll(pageable)
                    .map(product -> new ProductSearchResponse(product));
        }
        log.info("전체 상품 조회 결과: {}", page.getContent());
        return page;
    }// end of public Page<Product> pagingProductList(ProductSearchRequest request) -----------------


    // 페이징 처리 된 계절별 조회 상품리스트
    public Page<ProductSearchResponse> pagingProductListBySeason(ProductSearchRequest request) {
        log.info("계절별 상품조회 요청 정보: {}", request);

        Pageable pageable;
        Page<ProductSearchResponse> page;   // 초기화
        String season = request.getSeason();
        String searchProduct = request.getProduct();

        // 지정한 계절 이외의 것을 조회요청 했을 경우 전체 조회
        if (season == null || season.isEmpty()) {
            return pagingProductList(request);
        }

        // 검색 키워드가 있을 경우 와 없을 경우를 분기
        if (searchProduct != null && !searchProduct.isEmpty()) {
            log.info("계절별 검색: {}, 검색 키워드: {}", season, searchProduct);
            // 검색 키워드가 있을 경우
            pageable = request.toPageable(ProductSearchRequest.SORT_FIELD_NATIVE);
            page = productRepository.findBySeasonAndNameContaining(searchProduct, season, pageable)
                    .map(product -> new ProductSearchResponse(product)); // 검색 키워드와 페이징 처리로 조회  네이티브 쿼리 쓰면 enum 타입 변수로 사용 불가..
        }
        else {
            log.info("계절별 조회: {}", season);
            // 검색 키워드가 없을 경우
            pageable = request.toPageable(ProductSearchRequest.SORT_FIELD_JPQL);
            page = productRepository.findBySeason(ProductSeasons.valueOf(season), pageable)
                    .map(product -> new ProductSearchResponse(product));
        }

        log.info("계절별 상품 조회 결과: {}", page.getContent());
        return page;
    }// end of public Page<ProductSearchResponse> pagingProductListBySeason(ProductSearchRequest request) -------------------


    // 상품 한개의 정보를 가져오는 메소드
    public ProductDetailResponse findById(Long no) {
        ProductDetailResponse productDetail =  productRepository.findById(no)
                .map(product -> new ProductDetailResponse(product))
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품정보 입니다."));
        log.info("조회한 상품 정보: {}", productDetail);
        return productDetail;
    }// end of public ProductDetailResponse findById (Long no) ---------------------------


    // 단일 Product Entity 객체를 반환하는 메소드 (비즈니스 로직용)
    public Product getEntityById(Long productNo) {
        return productRepository.findById(productNo)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 상품정보 입니다."));
    }// end of public Product getEntityById(Long productNo) ---------------


    // 복수 Product Entity 객체를 반환하는 메소드 (비즈니스 로직용)
    public List<Product> getEntityListByIds(List<Long> productNos) {
        if (productNos == null || productNos.isEmpty()) {
            throw new IllegalArgumentException("유효하지 않은 상품정보 요청입니다.");
        }
        return productRepository.findAllById(productNos);
    }// end of public List<Product> getEntityListByIds(List<Long> productNos) ------------------------
}
