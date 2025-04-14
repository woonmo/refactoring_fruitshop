package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.controller.user.dto.LoginRequest;
import com.spring.refruitshop.controller.user.dto.LoginUser;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.user.UserService;
import jakarta.servlet.http.HttpServletRequest;
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

    // 로그인 처리
    @PostMapping("/login")
    public ResponseEntity<LoginUser> login(HttpServletRequest request, @RequestBody LoginRequest loginRequest) {
        LoginUser user = userService.login(request, loginRequest);
        return ResponseEntity.ok(user);
    }// end of public String login(HttpServletRequest request, LoginRequest loginRequest) ----------------------

}
