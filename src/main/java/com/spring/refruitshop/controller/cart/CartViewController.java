package com.spring.refruitshop.controller.cart;

import com.spring.refruitshop.dto.cart.CartItemResponse;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.cart.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
public class CartViewController {


    private final CartService cartService;

    public CartViewController(CartService cartService) {
        this.cartService = cartService;
    }

    // 장바구니 보기
    @GetMapping("/carts")
    public String viewCarts(Model model, @ModelAttribute("loginUser") User loginUser) {
        List<CartItemResponse> cartItemList = cartService.findAll(loginUser);

        model.addAttribute("cartItemList", cartItemList);

        return "cart/cartList";
    }// end of public String viewCarts(Model model) --------------------
}
