package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.dto.user.*;
import com.spring.refruitshop.service.cart.CartService;
import com.spring.refruitshop.service.user.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@Slf4j
public class UserApiController {

    private final UserService userService;
    private final CartService cartService;

    public UserApiController(UserService userService, CartService cartService) {
        this.userService = userService;
        this.cartService = cartService;
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
        if (loginUser == null) {
            return ResponseEntity.ok().body(null);
        }

        return ResponseEntity.ok().body(new LoginUser(loginUser));
    }// end of public ResponseEntity<LoginUser> getMyInfo(@ModelAttribute("loginUser") User loginUser) ----------------


    // 아이디 & 이메일 중복검사
    @PostMapping("/duplicate")
    public ResponseEntity<UserDuplicateResponse> userDuplicateCheck(@RequestBody UserDuplicateRequest request) {
        UserDuplicateResponse duplicateResponse = userService.duplicateCheckUserIdAndEmail(request);

        log.info("아이디 & 이메일 중복 여부: {}", duplicateResponse);

        return ResponseEntity.ok().body(duplicateResponse);
    }// end of public ResponseEntity<UserDuplicateResponse> userDuplicateCheck(@RequestBody UserDuplicateRequest request) ------------------

}
