package com.spring.refruitshop.repository.cart;

import com.spring.refruitshop.domain.cart.Cart;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByUserNoAndProductNo(@NotNull(message = "회원 정보는 필수입니다 !!") Long userNo, @NotNull(message = "상품 정보는 필수입니다 !!") Long productNo);

    @Query("SELECT DISTINCT c FROM Cart c JOIN FETCH c.user JOIN FETCH c.product WHERE c.user.no = :userNo")
    List<Cart> findAllByUserNo(@Param("userNo") Long no);    // 회원의 장바구니에 담긴 모든 정보


    @Modifying  // 데이터 수정임을 알려주는 어노테이션
    @Query("DELETE FROM Cart c WHERE c.user.no = :userNo")
    void deleteByUserNo(@Param("userNo") Long no);   // 장바구니 비우기

    int countByUserNo(Long no); // 로그인한 회원의 장바구니 개수 정보
}
