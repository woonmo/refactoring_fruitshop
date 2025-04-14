package com.spring.refruitshop.service.cart;

import com.spring.refruitshop.controller.cart.dto.AddItemRequest;
import com.spring.refruitshop.controller.cart.dto.AddItemResponse;
import com.spring.refruitshop.domain.cart.Cart;
import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.cart.CartRepository;
import com.spring.refruitshop.repository.product.ProductRepository;
import com.spring.refruitshop.repository.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
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
        }
        else {
            // 없으면 새로 카트 생성
            cart = Cart.builder()
                    .user(user)
                    .product(product)
                    .quantity(dto.getQuantity())
                    .build();
        }

        // 카트 저장
        cart = cartRepository.save(cart);

        return new AddItemResponse(cart);   // 카트 정보 반환
    }// end of public CartDTO save(AddItemRequest dto) ---------------------------------
}
