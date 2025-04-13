package com.spring.refruitshop.service.user;

import com.spring.refruitshop.controller.user.dto.UserRegisterRequest;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.repository.user.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // 회원가입
    public User save(UserRegisterRequest request) {
        // 아이디 중복 검사
        if (userRepository.existsByUserId(request.getUserid())) {
            log.error("UserId is Exist: {}", userRepository.existsByUserId(request.getUserid()));
            throw new IllegalArgumentException("이미 사용중인 아이디입니다.");
        }

        // 이메일 중복 검사
        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Email is Exist: {}", userRepository.existsByEmail(request.getEmail()));
            throw new IllegalArgumentException("이미 사용중인 이메일입니다.");
        }

        return userRepository.save(request.toEntity());
    }// end of public User save(UserRegisterRequest request) -----------------





}
