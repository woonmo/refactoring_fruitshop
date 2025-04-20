package com.spring.refruitshop.dto.user;

import com.spring.refruitshop.domain.user.User;
import lombok.Getter;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
public class LoginUser {

    private Long userNo;        // 회원번호

    private String userId;      // 아이디

    private String name;        // 이름

    private String birthday;    // 생일

    private String tel;         // 연락처

    private String email;       // 이메일

    private String zipcode;     // 우편번호

    private String address;     // 주소

    private String detailAddress;   // 상세주소

    private String extraAddress;    // 참고사항

    private String gender;      // 성별 (남, 여)

    private int point;          // 적립금

    private String createdAt;   // 가입일자

    public LoginUser(User user) {
        this.userNo = user.getNo();
        this.userId = user.getUserId();
        this.name = user.getName();
        this.birthday = user.getBirthday().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        this.tel = user.getTel();
        this.email = user.getEmail();
        this.zipcode = user.getAddress().getZipcode();
        this.address = user.getAddress().getAddress();
        this.detailAddress = user.getAddress().getDetailAddress();
        this.extraAddress = user.getAddress().getExtraAddress();
        this.gender = user.getGender().toString();
        this.point = user.getPoint();
        this.createdAt = user.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
