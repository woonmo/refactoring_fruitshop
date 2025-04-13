package com.spring.refruitshop.service.user;

import com.spring.refruitshop.controller.user.dto.UserRegisterRequest;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.domain.user.UserGender;
import com.spring.refruitshop.repository.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


//@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private UserRegisterRequest request;

    @BeforeEach
    void setUp() {
        request = new UserRegisterRequest();
        request.setUserid("honggildong");
        request.setPassword("password");
        request.setEmail("test@test.com");
        request.setName("홍길동");
        request.setTel("010-1234-1234");
        request.setGender(UserGender.여);
        request.setZipcode("12344");
        request.setAddress("경기도 하남시");
        request.setDetailAddress("213번지");
        request.setExtraAddress("무슨동");
        request.setBirthday("2000-03-23");
    }

    @DisplayName("userRegisterSuccess: 회원가입에 성공한다.")
    @Test
    public void userRegisterSuccess() {
        // given
        when(userRepository.existsByUserId("honggildong")).thenReturn(false);   // honggildong 이라는 id를 사용하지 않고 있다고 가정
        when(userRepository.existsByEmail("test@test.com")).thenReturn(false);  // test@test.com 이라는 email을 사용하지 않고 있다고 가정
        User savedUser = request.toEntity();        // 유저가 생성되었다고 가정
        savedUser.increaseUserNoTest(1L);           // 생성 시 유저번호 시퀀스가 1 증가하므로 유저번호 증가
        when(userRepository.save(any(User.class))).thenReturn(savedUser);          // User 엔티티 객체 저장 시 타입은 User 객체로 리턴한다고 가정

        // when: 회원가입을 진행한다.
        User user = userService.save(request);


        // then: 등록한 회원과 입력한 데이터가 일치하는지 검증한다.
        assertThat(user).isNotNull();   // 객체가 있는지
        assertThat(user.getUserId()).isEqualTo(request.getUserid());        // 유저 아이디 동일한지 검사
        assertThat(user.getPassword()).isEqualTo(request.getPassword());    // PW 검사 시큐리티 쓰면 못할지도?
        assertThat(user.getEmail()).isEqualTo(request.getEmail());          // 이메일
        assertThat(user.getName()).isEqualTo(request.getName());            // 이름
        assertThat(user.getTel()).isEqualTo(request.getTel());              // 전화번호
        assertThat(user.getGender()).isEqualTo(request.getGender());        // 성별
        assertThat(user.getAddress().getZipcode()).isEqualTo(request.getZipcode()); // 우편번호
        assertThat(user.getAddress().getAddress()).isEqualTo(request.getAddress()); // 주소
        assertThat(user.getAddress().getDetailAddress()).isEqualTo(request.getDetailAddress()); // 상세주소
        assertThat(user.getAddress().getExtraAddress()).isEqualTo(request.getExtraAddress());   // 참고사항
        assertThat(user.getBirthday()).isEqualTo(request.getBirthday());    // 생일

    }

}