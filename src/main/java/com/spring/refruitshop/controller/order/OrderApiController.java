package com.spring.refruitshop.controller.order;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.order.OrderDraft;
import com.spring.refruitshop.dto.order.OrderInitRequest;
import com.spring.refruitshop.service.order.OrderService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class OrderApiController {

    private OrderService orderService;

    public OrderApiController(OrderService orderService) {
        this.orderService = orderService;
    }

    // 주문서를 생성해주는 메소드
    @PostMapping("/api/orders/initiate")
    public ResponseEntity<Map<String, String>> initiateOrder(@RequestBody OrderInitRequest request, HttpSession session
                                         , @ModelAttribute("loginUser") User loginUser) {
        OrderDraft draft = orderService.prepareOrder(request, loginUser);
        if (draft == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        session.setAttribute("draft", draft);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("draftId", draft.getDraftId()));
    }// end of public ResponseEntity<OrderDraft> initiateOrder(@RequestBody OrderInitRequest request, HttpSession session, @ModelAttribute("loginUser") User loginUser) ----------------

}
