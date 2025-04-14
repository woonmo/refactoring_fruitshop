package com.spring.refruitshop.service.cart;

import com.spring.refruitshop.controller.cart.dto.AddItemRequest;
import com.spring.refruitshop.controller.cart.dto.AddItemResponse;
import com.spring.refruitshop.controller.user.dto.LoginUser;
import com.spring.refruitshop.domain.cart.Cart;
import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.cart.CartRepository;
import com.spring.refruitshop.repository.product.ProductRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import com.spring.refruitshop.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;

import java.nio.file.AccessDeniedException;
import java.util.Optional;

@Service
@Slf4j
public class CartService {

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public CartService(CartRepository cartRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }


    // 장바구니에 상품 & 수정 추가 메소드
    @Transactional
    public AddItemResponse save(AddItemRequest dto) {
        // 유효한 유저인지 검증
        User user = userRepository.findById(dto.getUserNo())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        // 유효한 상품인지 검증
        Product product = productRepository.findById(dto.getProductNo())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));

        // 먼저 등록한 상품인지 확인 후 있으면 update, 없으면 insert 한다
        Optional<Cart> existCart = cartRepository.findByUserNoAndProductNo(dto.getUserNo(), dto.getProductNo());

        Cart cart;
        if (existCart.isPresent()) {
            // 장바구니에 담은 것이 있으면
            cart = existCart.get();
            cart.updateQuantity(dto.getQuantity());   // 수량 업데이트
            log.info("Updated Cart info: {}", cart);
        }
        else {
            // 없으면 새로 카트 생성
            cart = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
            log.info("Add Cart info: {}", cart);
        }

        // 카트 저장
        cart = cartRepository.save(cart);

        return new AddItemResponse(cart);   // 카트 정보 반환
    }// end of public CartDTO save(AddItemRequest dto) ---------------------------------


    // 장바구니 항목 삭제
    @Transactional
    public void delete(Long id) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니입니다."));

        // 본인 장바구니인지 검증
        authorizeCartOwner(cart);

        // 삭제 진행
        cartRepository.delete(cart);
    }// end of public void delete(Long id) ----------------------


    // 본인의 장바구니가 맞는지 확인
    private void authorizeCartOwner(Cart cart) {
        // 세션 가져오기
        HttpSession session = SessionUtil.getSession();

        LoginUser loginuser = (LoginUser) session.getAttribute("user");

        // 추후 스프링 시큐리티 적용시 수정해야 함
        User user = userRepository.findById(cart.getUser().getNo())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 유저입니다."));

        if (loginuser.getUserNo() != user.getNo()) {
            // 회원번호가 같지 않다면(다른 회원의 장바구니를 삭제 시도)
            throw new IllegalArgumentException("다른 회원의 장바구니입니다.");
        }

    }// end of private void authorizeCartOwner(Cart cart) --------------------------
}
