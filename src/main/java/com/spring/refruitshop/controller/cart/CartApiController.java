package com.spring.refruitshop.controller.cart;

import com.spring.refruitshop.controller.cart.dto.AddItemRequest;
import com.spring.refruitshop.controller.cart.dto.AddItemResponse;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.cart.CartService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartApiController {

    private final CartService cartService;

    public CartApiController(CartService cartService) {
        this.cartService = cartService;
    }


    // 장바구니에 추가 & 수정
    @PostMapping
    public ResponseEntity<AddItemResponse> addItemCart(@RequestBody @Validated AddItemRequest request,
                                                       @ModelAttribute("loginUser") User loginUser) {


        AddItemResponse addItemResponse = cartService.save(request, loginUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(addItemResponse);
    }// end of public ResponseEntity<CartDTO> addCart(@RequestBody AddItemRequest request) ----------------------


    // 장바구니 항목 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteItemCart(@PathVariable Long id) {
        cartService.delete(id);

        return ResponseEntity.ok().body("장바구니 항목이 삭제되었습니다.");
    }// end of public ResponseEntity<Void> deleteItemCart(@PathVariable Long id) ---------------------
}
