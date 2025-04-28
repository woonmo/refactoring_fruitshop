package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserViewController {

    private final UserService userService;

    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    // 메인 페이지
    @GetMapping("/")
    public String index() {
        return "index";
    }

    // 로그인 페이지
    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        // 로그인 시 이전 페이지로 돌아갈 수 있게끔 URL 저장
        String referer = request.getHeader("referer");

        if(referer == null || referer.endsWith("/login") || referer.endsWith("/login?loginFail=true")) {
            referer = "/";
        }

        HttpSession session = request.getSession();
        session.setAttribute("prevURLPage", referer);

        // login 실패여부 체크하기
        String loginFail = request.getParameter("loginFail");

        if("true".equals(loginFail)) {
            String loginFailMessage = "아이디 또는 비밀번호를 확인하세요";
            session.setAttribute("prevURLPage", "/");
            request.setAttribute("loginFailMessage", loginFailMessage);
        }

        return "user/login";
    }

    // 회원가입 페이지
    @GetMapping("/signUp")
    public String signUp() {
        return "user/signUp";
    }// end of public String signUp() ------------

    // 휴면 복구 페이지
    @GetMapping("/idle")
    public String idle() {
        return "idle";
    }

}
