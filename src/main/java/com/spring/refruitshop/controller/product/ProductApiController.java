package com.spring.refruitshop.controller.product;

import com.spring.refruitshop.dto.product.ProductRegisterRequest;
import com.spring.refruitshop.dto.product.ProductRegisterResponse;
import com.spring.refruitshop.service.product.ProductService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/products")
public class ProductApiController {

    private final ProductService productService;

    public ProductApiController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductRegisterResponse> registerProduct(@RequestBody @Validated ProductRegisterRequest request) {
        ProductRegisterResponse productRegisterResponse = productService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(productRegisterResponse);
    }// end of public ResponseEntity<ProductRegisterResponse> registerProduct(@RequestBody @Validated ProductRegisterRequest productRegisterRequest) ------------------------

}
