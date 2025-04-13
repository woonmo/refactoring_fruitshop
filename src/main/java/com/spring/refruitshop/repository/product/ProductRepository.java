package com.spring.refruitshop.repository.product;

import com.spring.refruitshop.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
