package com.spring.refruitshop.controller.user.dto;

import com.spring.refruitshop.domain.common.Address;
import com.spring.refruitshop.domain.user.User;
import com.spring.refruitshop.domain.user.UserGender;
import com.spring.refruitshop.domain.user.UserRole;
import com.spring.refruitshop.domain.user.UserStatus;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UserRegisterRequest {
    // 회원가입 요청 DTO

    @NotBlank(message = "아이디는 필수 입력사항입니다 !!")
    @Size(min = 5, max = 20, message = "아이디는 5~20 글자이어야 합니다.")
    private String userid;

    @NotBlank(message = "비밀번호는 필수 입력사항입니다!!")
//    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$",
//            message = "비밀번호는 8자 이상, 문자/숫자/특수문자를 포함해야 합니다.")
    private String password;

    @NotBlank(message = "이름은 필수 입력사항입니다 !!")
    @Size(min = 2, max = 10, message = "이름은 2~10 글자이어야 합니다.")
    private String name;

    @NotBlank(message = "이메일은 필수 입력사항입니다 !!")
    @Email(message = "유효한 이메일 형식을 입력해주세요 !!")
    private String email;

    @Pattern(regexp = "^010-\\d{4}-\\d{4}$", message = "전화번호는 010-XXXX-XXXX 형식이어야 합니다.")
    private String tel;

    private String zipcode;
    private String address;
    private String detailAddress;
    private String extraAddress;

    private UserGender gender;

    private String birthday;

    // User Entity 객체로 만들어주는 메소드
    public User toEntity() {
        Address _address = new Address(zipcode, address, detailAddress, extraAddress);

        return User.builder()
                .userId(userid)
                .password(password)
                .name(name)
                .email(email)
                .tel(tel)
                .address(_address)
                .gender(gender)
                .birthday(LocalDate.parse(birthday))
                .status(UserStatus.ACTIVE)
                .role(UserRole.USER)
                .build();
    }

}
