package com.spring.refruitshop.dto.user;

import jakarta.validation.constraints.*;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserInfoRequest {

    // 회원정보수정 요청 DTO
    @NotBlank(message = "아이디는 필수 입력사항입니다 !!")
    @Size(min = 5, max = 20, message = "아이디는 5~20 글자이어야 합니다.")
    private String userid;

//    @NotBlank(message = "비밀번호는 필수 입력사항입니다!!")
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



    @Override
    public String toString() {
        return "UpdateUserInfoRequest{" +
                "userid='" + userid + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", tel='" + tel + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", address='" + address + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", extraAddress='" + extraAddress + '\'' +
                '}';
    }
}
