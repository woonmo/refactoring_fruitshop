package com.spring.refruitshop.controller.cart;

import com.spring.refruitshop.controller.cart.dto.AddItemRequest;
import com.spring.refruitshop.controller.cart.dto.CartDTO;
import com.spring.refruitshop.service.cart.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
public class CartApiController {

    private final CartService cartService;

    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }


    // 장바구니에 추가
    @PostMapping
    public ResponseEntity<CartDTO> addItemCart(@RequestBody @Validated AddItemRequest request) {
        CartDTO cartDTO = cartService.save(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(cartDTO);
    }// end of public ResponseEntity<CartDTO> addCart(@RequestBody AddItemRequest request) ----------------------

}
