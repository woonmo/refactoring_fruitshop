package com.spring.refruitshop.controller.user;

import com.spring.refruitshop.controller.user.dto.UserDTO;
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
    public ResponseEntity<UserDTO> registerUser(@RequestBody @Validated UserRegisterRequest request) {
        User user = userService.save(request);

        // 회원 정보를 리턴
        return ResponseEntity.status(HttpStatus.CREATED).body(new UserDTO(user));
        /*
            {
                "userId": "hongildong1",
                "name": "홍길동",
                "birthday": "1993-02-12",
                "tel": "010-2322-3211",
                "email": "test@tes1t.com",
                "zipcode": "12345",
                "address": "안산시",
                "detailAddress": "김김동",
                "extraAddress": "아아동",
                "gender": "남",
                "point": 0,
                "createdAt": "2025-04-13"
            }
         */
    }// end of public ResponseEntity<User> registerUser(@RequestBody UserRegisterRequest request) ---------------


}
