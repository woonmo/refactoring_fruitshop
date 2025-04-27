package com.spring.refruitshop.controller.cart;

import com.spring.refruitshop.dto.cart.AddItemRequest;
import com.spring.refruitshop.dto.cart.AddItemResponse;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.cart.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/carts")
public class CartApiController {

    private final CartService cartService;
    private final CartSSEController cartSSEController;

    public CartApiController(CartService cartService, CartSSEController cartSSEController) {
        this.cartService = cartService;
        this.cartSSEController = cartSSEController;
    }


    // 장바구니에 추가 & 수정
    @PostMapping
    public ResponseEntity<AddItemResponse> addItemCart(@RequestBody @Validated AddItemRequest request,
                                                       @ModelAttribute("loginUser") User loginUser, HttpSession session) {
        AddItemResponse addItemResponse = cartService.save(request, loginUser, session);

        // 클라이언트에 장바구니 변경 정보 전달
        cartSSEController.notifyCartUpdate();

        return ResponseEntity.status(HttpStatus.OK).body(addItemResponse);
    }// end of public ResponseEntity<CartDTO> addCart(@RequestBody AddItemRequest request) ----------------------


    // 장바구니 항목 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteItemCart(@PathVariable Long id, @ModelAttribute("loginUser") User loginUser,
                                                 HttpSession session) {
        cartService.delete(id, loginUser, session);

        // 클라이언트에 장바구니 변경 정보 전달
        cartSSEController.notifyCartUpdate();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "장바구니 항목이 삭제되었습니다."));
    }// end of public ResponseEntity<Void> deleteItemCart(@PathVariable Long id) ---------------------


    // 장바구니 비우기
    @DeleteMapping
    public ResponseEntity<Map<String, String>> emptyCart(@ModelAttribute("loginUser") User loginUser, HttpSession session) {
        cartService.deleteAll(loginUser, session);

        // 클라이언트에 장바구니 변경 정보 전달
        cartSSEController.notifyCartUpdate();

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "장바구니를 비웠습니다."));
    }// end of public ResponseEntity<String> emptyCart(@ModelAttribute("loginUser") User loginUser) --------------------------
}
