package com.spring.refruitshop.repository.product;

import com.spring.refruitshop.domain.product.Product;
import com.spring.refruitshop.domain.product.ProductSeasons;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {

    Optional<Product> findByNameAndPrice(@NotBlank(message = "상품명은 필수입력 사항입니다 !!") String name, @NotBlank(message = "상품 판매가는 필수입력 사항입니다 !!") @Min(value = 0, message = "상품 판매가는 0 이상이어야 합니다 !!") int price);

    // 계절별로 조회
    Page<Product> findBySeason(ProductSeasons season, Pageable pageable);



    /*
        Native Query
        SELECT tbl_product columns
          FROM tbl_product p
         WHERE upper(p.prod_name) LIKE '%'||upper(searchProduct)||'%'
     */
    @Query(
            value = "SELECT * FROM products p WHERE UPPER(p.prod_name) LIKE '%'|| :searchProduct || '%'",
            countQuery = "SELECT COUNT(*) FROM products p WHERE UPPER(p.prod_name) LIKE '%'|| :searchProduct || '%'",
            nativeQuery = true
    )
    Page<Product> findByNameContaining(@Param("searchProduct") String searchProduct, Pageable pageable);  // 전체 상품에서 검색


    /*
        Native Query
        SELECT tbl_product columns
          FROM tbl_product p
         WHERE p.prod_season = season
           AND upper(p.prod_name) LIKE '%'||upper(searchProduct)||'%'
     */
//    @Query("SELECT p FROM Product p WHERE p.season = :season AND UPPER(p.name) LIKE CONCAT('%', :searchProduct, '%')")
    @Query(
            value = "SELECT * FROM products p WHERE p.prod_season = :season AND UPPER(p.prod_name) LIKE CONCAT('%', :searchProduct, '%')",
            countQuery = "SELECT COUNT(*) FROM products p WHERE p.prod_season = :season AND UPPER(p.prod_name) LIKE CONCAT('%', :searchProduct, '%')",
            nativeQuery = true
    )
    Page<Product> findBySeasonAndNameContaining(@Param("searchProduct") String searchProduct, @Param("season") String season, Pageable pageable); // 계절별 상품 조회에서 검색
}
