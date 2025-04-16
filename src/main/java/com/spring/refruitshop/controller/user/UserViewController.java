package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.controller.user.dto.LoginRequest;
import com.spring.refruitshop.controller.user.dto.LoginUser;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
        if(referer.endsWith("/security/member/login") || referer.endsWith("/security/member/login?loginFail=true")) {
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

        return "/user/login";
    }

    // 로그인 처리
//    @PostMapping("/login")
//    public ResponseEntity<LoginUser> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
//        // 로그인 기록 DB 에 남기기
//        LoginUser user = userService.login(request, loginRequest);
//        return ResponseEntity.ok(user);
//    }// end of public String login(HttpServletRequest request, LoginRequest loginRequest) ----------------------


    // 휴면 복구 페이지
    @GetMapping("/idle")
    public String idle() {
        return "idle";
    }

}
