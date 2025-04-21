package com.spring.refruitshop.controller.order;

import com.spring.refruitshop.dto.order.OrderDraft;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class OrderViewController {


    // 주문서 페이지로 이동
    @GetMapping("/orders/draft/{draftId}")
    public String draft(Model model, @PathVariable String draftId, HttpSession session) {
        OrderDraft draft = (OrderDraft) session.getAttribute("draft");
        log.info("받아온 주문서정보: {}, 넘어온 주문서번호: {}, 일치여부: {}", draft, draftId, draft.getDraftId().equals(draftId));


        if (draft == null || !draft.getDraftId().equals(draftId)) {
            throw new IllegalArgumentException("유효하지 않은 주문서입니다.");
        }

        model.addAttribute("draft", draft);
        return "order/orderDraft";
    }// end of public String draft(Model model, @PathVariable String draftId, HttpSession session) ----------------

}
