package com.spring.refruitshop.domain.user;

import com.spring.refruitshop.domain.cart.Cart;
import com.spring.refruitshop.domain.common.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User {
    /**
     * 회원 Entity 클래스 파일
     */

    @Id
    @Column(unique = true, nullable = false, name = "user_no")
    @SequenceGenerator(name="SEQ_USER_GENERATOR", sequenceName="user_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_USER_GENERATOR")
    private Long no;        // 유저번호, 시퀀스

    @Column(name = "user_id", nullable = false, length = 20)
    private String userId;      // 아이디

    @Column(name = "password", nullable = false)
    private String password;    // 비밀번호

    @Column(name = "name", nullable = false)
    private String name;        // 이름

    @Column(name = "birthday")
    private LocalDate birthday;    // 생일

    @Column(name = "tel")
    private String tel;         // 연락처

    @Column(name = "email", unique = true, nullable = false)
    private String email;       // 이메일

    @Embedded
    private Address address;    // 주소

    @Enumerated(EnumType.STRING)
    private UserGender gender;          // 성별 (남, 여)

    @Column(name = "point")
    @ColumnDefault("0")
    @Builder.Default
    private int point = 0;         // 적립금

    @CreatedDate    // 생성 시간
    @Column(name = "created_at", updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();  // 가입일자

    @LastModifiedDate
    @Column(name = "lastpwdchangedate")
    @Builder.Default
    private LocalDateTime lastPwdChangeDate = LocalDateTime.now();    // 마지막 비밀번호 변경일자

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Cart> carts;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private UserStatus status; // 회원상태 (ACTIVE, IDLE, BLOCKED, DELETED)

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private UserRole role;   // 회원권한(USER, ADMIN)

    @PrePersist
    public void prePersist() {
        if (carts == null) {
            carts = new ArrayList<>();
        }
    }

    public void increaseUserNoTest(long no) {
        this.no = no;
    }

}
