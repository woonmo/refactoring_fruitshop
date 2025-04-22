package com.spring.refruitshop.dto.order;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderDraft {

    private String draftId;     // 임시 주문서 번호를 UUID 로 생성

    // 주문페이지에서 결제를 진행하기 전 확인하는 주문서 객체
    // 회원정보, 상품정보, 배송정보, 총가격, 할인가격, 최종결정가격, 배송요청사항

    // 1. 회원정보
    private Long userNo;    // 회원번호
    private String email;   // 주문자 이메일 (결제완료 메일 발송)
    private String name;    // 주문자 이름
    private String tel;     // 주문자 연락처

    // 2. 상품정보
    private List<OrderDraftItem> items;

    // 3. 배송정보
    private String receiverName;    // 받는이
    private String receiverTel;     // 받는이 연락처
    private String zipCode;         // 우편번호
    private String address;         // 주소
    private String detailAddress;   // 상세주소
    private String extraAddress;    // 참고사항

    // 4. 가격정보 (프론트에서 진행)
    private int totalPrice;     // 주문총액
    private int discount;       // 할인액
    private int finalPrice;     // 최종결제예정금액
    private int point;          // 적립금

    // 5. 배송요청사항
    private String requestNote;

    // 6. 기본 배송지 설정 여부
    private String isDefaultShip;


    // 7. 주문서 생성 시간(5분간 유효)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();



    // 클라이언트의 주문서 입력 정보를 업데이트하는 메소드
    public void updateDraft(OrderConfirmRequest request) {
        // 배송지 주소관련
        this.zipCode = request.getZipCode();
        this.address = request.getAddress();
        this.detailAddress = request.getDetailAddress();
        this.extraAddress = request.getExtraAddress();

        // 결제 금액 관련
        this.totalPrice = request.getTotalPrice();
        this.discount = request.getDiscount();
        this.finalPrice = request.getFinalPrice();
        this.point = request.getPoint();

        // 수신인 관련
        this.receiverName = request.getReceiverName();
        this.receiverTel = request.getReceiverTel();

        // 요청 사항
        this.requestNote = request.getRequestNote();

        // 기본 배송지 설정 여부
        this.isDefaultShip = request.getIsDefaultShip();
    }

    @Override
    public String toString() {
        return "OrderDraft{" +
                "draftId='" + draftId + '\'' +
                ", userNo=" + userNo +
                ", email='" + email + '\'' +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", items=" + items +
                ", receiverName='" + receiverName + '\'' +
                ", receiverTel='" + receiverTel + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", address='" + address + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", extraAddress='" + extraAddress + '\'' +
                ", totalPrice=" + totalPrice +
                ", discount=" + discount +
                ", finalPrice=" + finalPrice +
                ", point=" + point +
                ", requestNote='" + requestNote + '\'' +
                ", isDefaultShip='" + isDefaultShip + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
