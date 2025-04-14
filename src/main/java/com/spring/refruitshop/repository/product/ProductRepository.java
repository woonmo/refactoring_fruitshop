package com.spring.refruitshop.repository.product;

import com.spring.refruitshop.domain.product.Product;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameAndPrice(@NotBlank(message = "상품명은 필수입력 사항입니다 !!") String name, @NotBlank(message = "상품 판매가는 필수입력 사항입니다 !!") @Min(value = 0, message = "상품 판매가는 0 이상이어야 합니다 !!") int price);
}
