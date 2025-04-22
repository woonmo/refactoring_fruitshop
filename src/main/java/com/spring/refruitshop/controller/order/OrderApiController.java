package com.spring.refruitshop.controller.order;

import com.spring.refruitshop.domain.order.Order;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.order.OrderConfirmRequest;
import com.spring.refruitshop.dto.order.OrderDraft;
import com.spring.refruitshop.dto.order.OrderInitRequest;
import com.spring.refruitshop.service.order.OrderService;
import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
public class OrderApiController {

    private OrderService orderService;
    private UserService userService;

    public OrderApiController(OrderService orderService, UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
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

        log.info("생성된 주문서 정보: {}", draft);

        return ResponseEntity.status(HttpStatus.OK).body(Map.of("draftId", draft.getDraftId()));
    }// end of public ResponseEntity<OrderDraft> initiateOrder(@RequestBody OrderInitRequest request, HttpSession session, @ModelAttribute("loginUser") User loginUser) ----------------



    // 주문을 처리해주는 메소드
    @PostMapping("/api/orders/confirm")
    public ResponseEntity<?> confirmOrder(@RequestBody OrderConfirmRequest request, HttpSession session
                                        , @ModelAttribute("loginUser") User loginUser) {

        // 세션에 있는 주문서 가져오기
        OrderDraft draft = (OrderDraft) session.getAttribute("draft");

        if (draft == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("주문 정보가 없습니다.");
        }

        // 클라이언트 입력 정보를 주문서에 업데이트
        draft.updateDraft(request);

        log.info("최종 주문 요청 정보: {}", draft);

        Order order = orderService.confirmOrder(draft, loginUser, session);

        session.removeAttribute("draft");

        // 세션 업데이트를 위한 유저 정보 조회
        User updatedUser = userService.getEntityById(loginUser.getNo());
        session.setAttribute("loginUser", updatedUser);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("orderCode", order.getOrderCode()));
    }// end of public ResponseEntity<?>  confirmOrder () -----------------------------

}
