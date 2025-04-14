package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.controller.user.dto.UserRegisterResponse;
import com.spring.refruitshop.controller.user.dto.UserRegisterRequest;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.service.user.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
