package com.spring.refruitshop.domain.common;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
public class Address {

    @Column(name = "zipcode")
    private String zipcode;         // 우편번호

    @Column(name = "address")
    private String address;         // 주소

    @Column(name = "detail_address")
    private String detailAddress;   // 상세주소

    @Column(name = "extra_address")
    private String extraAddress;    // 참고사항


    public Address(String zipcode, String address, String detailAddress, String extraAddress) {
        this.zipcode = zipcode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.extraAddress = extraAddress;
    }
}
