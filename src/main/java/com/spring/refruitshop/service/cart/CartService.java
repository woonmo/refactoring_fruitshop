package com.spring.refruitshop.service.cart;

import com.spring.refruitshop.controller.cart.dto.AddItemRequest;
import com.spring.refruitshop.controller.cart.dto.AddItemResponse;
import com.spring.refruitshop.controller.cart.dto.CartItemResponse;
import com.spring.refruitshop.domain.cart.Cart;
import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.cart.CartRepository;
import com.spring.refruitshop.repository.product.ProductRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
    public AddItemResponse save(AddItemRequest dto, User loginUser) {

        // 유효한 상품인지 검증
        Product product = productRepository.findById(dto.getProductNo())
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 상품입니다."));

        // 먼저 등록한 상품인지 확인 후 있으면 update, 없으면 insert 한다
        Cart cart = cartRepository.findByUserNoAndProductNo(loginUser.getNo(), dto.getProductNo())
                .map(existCart -> {
                    // map 은 Optional 에서 정보가 있으면 실행
                    // 하나의 메소드이며 지금 Cart 객체로 리턴하므로 해당 객체를 리턴해줘야 함
                    existCart.updateQuantity(dto.getQuantity());
                    log.info("Updated Cart info: {}", existCart);
                    return existCart;
                })
                .orElseGet(() -> {
                    // orElse 는 Optional 에서 값이 없으면 실행
                    Cart newCart = Cart.builder()
                        .user(loginUser)
                        .product(product)
                        .quantity(dto.getQuantity())
                        .build();
                    log.info("Add Cart info: {}", newCart);
                    return newCart;
                });

        // 카트 저장
        cart = cartRepository.save(cart);

        return new AddItemResponse(cart);   // 카트 정보 반환
    }// end of public CartDTO save(AddItemRequest dto) ---------------------------------


    // 장바구니 항목 삭제
    @Transactional
    public void delete(Long id, User loginUser) {
        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 장바구니입니다."));

        // 본인 장바구니인지 검증
        if (cart.getUser().getNo() != loginUser.getNo()) {
            throw new IllegalArgumentException("다른 회원의 장바구니입니다.");
        }

        // 삭제 진행
        cartRepository.delete(cart);
    }// end of public void delete(Long id) ----------------------


    // 회원의 장바구니 목록을 가져온다.
    @Transactional(readOnly = true) // Select 용
    // 엔티티에서 fetch 지연 로딩을 사용할 경우 한 트랜잭션 내에 있어야 정보 조회가 가능 -> 실제 값에 접근할 때 데이터베이스에 접근하기 때문
    public List<CartItemResponse> findAll(User loginUser) {
        List<Cart> cartList = cartRepository.findAllByUserNo(loginUser.getNo());
        log.info("장바구니 항목 개수: {}", cartList.size());

        return cartList
                .stream()
                .map(this::addCartItemResponseWithValidation)     // 이 클래스의 addCartItemResponse 메소드 실행 (메소드 참조) 데이터 무결성 검사
                .collect(Collectors.toList());
    }// end of public List<CartItemResponse> findAll(User loginUser) -----------------------


    // 조회한 장바구니 목록의 유효여부 검사
    private CartItemResponse addCartItemResponseWithValidation(Cart cart) {
        if (cart == null) {
            throw new IllegalArgumentException("존재하지 않는 장바구니 입니다.");
        }
        if (cart.getUser() == null) {
            throw new IllegalStateException("유효하지 않은 회원입니다.");
        }
        if (cart.getProduct() == null) {
            throw new IllegalStateException("존재하지 않은 상품입니다.");
        }
        if (cart.getQuantity() <= 0) {
            throw new IllegalStateException("수량 정보가 유하지 않습니다. 수량: "+ cart.getQuantity());
        }
        if (cart.getProduct().getPrice() < 0) {
            throw new IllegalStateException("가격 정보가 유하지 않습니다. 가격: "+ cart.getProduct().getPrice());
        }

        return new CartItemResponse(cart);
    }// end of private CartItemResponse addCartItemResponse(Cart cart) ------------------


    // 회원의 장바구니를 비운다.
    @Transactional
    public void deleteAll(User loginUser) {

        if (loginUser == null) {
            log.error("로그인 정보가 없습니다.");
            throw new IllegalArgumentException("로그인 정보가 없습니다.");
        }

        log.info("회원번호 {}번의 장바구니를 비웠습니다. ", loginUser.getNo());
        cartRepository.deleteByUserNo(loginUser.getNo());

    }// end of public void deleteAll(User loginUser) ------------------------
}
