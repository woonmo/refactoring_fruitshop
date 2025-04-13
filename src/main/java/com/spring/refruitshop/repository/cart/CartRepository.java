package com.spring.refruitshop.repository.cart;

import com.spring.refruitshop.domain.cart.Cart;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserNoAndProductNo(@NotNull(message = "회원 정보는 필수입니다 !!") Long userNo, @NotNull(message = "상품 정보는 필수입니다 !!") Long productNo);
}
