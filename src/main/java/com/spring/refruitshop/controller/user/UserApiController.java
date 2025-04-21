package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.user.LoginUser;
import com.spring.refruitshop.dto.user.UserRegisterResponse;
import com.spring.refruitshop.dto.user.UserRegisterRequest;
import com.spring.refruitshop.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserApiController {

    private final UserService userService;

    public UserApiController(UserService userService) {
        this.userService = userService;
    }


    // 회원 가입
    @PostMapping
    public ResponseEntity<UserRegisterResponse> registerUser(@RequestBody @Validated UserRegisterRequest request) {
        UserRegisterResponse createdUser = userService.save(request);

        // 가입된 회원 정보를 리턴
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }// end of public ResponseEntity<User> registerUser(@RequestBody UserRegisterRequest request) ---------------


    // 로그인 한 회원의 정보를 반환하는 메소드
    @GetMapping("/me")
    public ResponseEntity<LoginUser> getMyInfo(@ModelAttribute("loginUser") User loginUser) {
        return ResponseEntity.ok().body(new LoginUser(loginUser));
    }// end of public ResponseEntity<LoginUser> getMyInfo(@ModelAttribute("loginUser") User loginUser) ----------------

}
