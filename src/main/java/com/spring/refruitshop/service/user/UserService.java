package com.spring.refruitshop.service.user;

import com.spring.refruitshop.controller.user.dto.LoginRequest;
import com.spring.refruitshop.controller.user.dto.LoginUser;
import com.spring.refruitshop.controller.user.dto.UserRegisterRequest;
import com.spring.refruitshop.controller.user.dto.UserRegisterResponse;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.user.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원 가입
    public UserRegisterResponse save(UserRegisterRequest request) {
        // 아이디 중복 검사
        if (userRepository.existsByUserId(request.getUserid())) {
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        // 회원 등록
        User user = userRepository.save(request.toEntity());
        log.info("Created User: {}", user);

        return new UserRegisterResponse(user);
    }// end of public User save(UserRegisterRequest request) -----------------


    // 로그인 처리
    // 시큐리티 적용시 수정 필요
    public LoginUser login(HttpServletRequest request, LoginRequest loginRequest) {

        log.info("Login request: {}", loginRequest.getUserid());

        User user = userRepository.findByUserId(loginRequest.getUserid())
                .orElseThrow(() -> new IllegalArgumentException("아이디 혹은 비밀번호가 틀립니다."));

        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new IllegalArgumentException("아이디 혹은 비밀번호가 틀립니다.");
        }

        log.info("Login User: {}", user);
        LoginUser loginUser = new LoginUser(user);

        request.getSession().setAttribute("user", loginUser);
        return loginUser;
    }// end of public void login(HttpServletRequest request, LoginRequest loginRequest) -------------------
}
