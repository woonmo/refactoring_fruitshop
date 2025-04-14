package com.spring.refruitshop.service.product;

import com.spring.refruitshop.controller.product.dto.ProductRegisterRequest;
import com.spring.refruitshop.controller.product.dto.ProductRegisterResponse;
import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.product.ProductRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
        log.info("Searched user that before register product: {}", user);

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
        log.info("Saved product: {}", product);

        // 객체 반환
        return new ProductRegisterResponse(product);
    }// end of public ProductRegisterResponse save(ProductRegisterRequest request) -----------------------------
}
