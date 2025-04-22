package com.spring.refruitshop.config;

import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.cart.CartRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    // Controller Advice 는 컨트롤러가 동작하기 전에 먼저 동작한다.
    // 공통 예외처리가 필요하거나(@ExceptionHandler), 공통 데이터바인딩을 하거나(@InitBinder) 공통 모델 속성 추가(@ModelAttribute)를 할 때 사용한다.

    @ModelAttribute("loginUser")
    public User loginUser(Authentication authentication) {
        // 로그인 되지 않은 유저는 null 을 리턴한다.
        // 이렇게 ModelAttribute 를 지정하면 뷰템플릿, 어느 컨트롤러에서든 loginUser 정보를 사용할 수 있다.
        if (authentication == null || !(authentication.getPrincipal() instanceof User)) {   // 인증 정보가 null 이거나 인증 객체가 User 가 아닌 경우
            return null;
        }
        return (User) authentication.getPrincipal();
    }// end of public User loginUser(Authentication authentication) ------------

}
