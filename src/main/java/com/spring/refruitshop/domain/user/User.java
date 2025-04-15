package com.spring.refruitshop.domain.user;

import com.spring.refruitshop.domain.cart.Cart;
import com.spring.refruitshop.domain.common.Address;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class User implements UserDetails {
    /**
     * 회원 Entity 클래스 파일
     */

    @Id
    @Column(unique = true, nullable = false, name = "user_no")
    @SequenceGenerator(name="SEQ_USER_GENERATOR", sequenceName="user_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_USER_GENERATOR")
    private Long no;        // 유저번호, 시퀀스

    @Column(name = "user_id", nullable = false, length = 20, unique = true)
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


    // === 스프링 시큐리티 시작 === //
    @Override   // 권한 반환
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_"+role.name()));     // 스프링 시큐리티는 ROLE_권한을 탐색하므로 꼭 "ROLE_" 를 붙여줘야 함.
    }

    // 사용자의 id를 반환(고유한 값)
    @Override
    public String getUsername() {
        return email;
    }

    // 사용자의 패스워드를 반환
    @Override
    public String getPassword() {
        return password;
    }


    // 계정 만료 여부 반환
    @Override
    public boolean isAccountNonExpired() {
        // 만료 되었는지 확인하는 로직
        return true;    // true -> 잠금되지 않음
    }

    // 계정 잠금 여부 반환
    @Override
    public boolean isAccountNonLocked() {
        // 계정 잠금 되었는지 확인하는 로직
        return true;    // true -> 잠금되지 않음
    }


    // 패스워드의 만료 여부 반환
    @Override
    public boolean isCredentialsNonExpired() {
        // 패스워드가 만료되었는지 확인하는 로직
        return true;    // true -> 만료되지 않음
    }

    @Override
    public boolean isEnabled() {
        // 계정이 사용 가능한지 확인하는 로직
        return true; // true -> 사용 가능
    }
    // === 스프링 시큐리티 끝 === //

    // logging
    @Override
    public String toString() {
        return "User{" +
                "no=" + no +
                ", userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", birthday=" + birthday +
                ", tel='" + tel + '\'' +
                ", email='" + email + '\'' +
                ", address=" + address +
                ", gender=" + gender +
                ", point=" + point +
                ", createdAt=" + createdAt.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ", lastPwdChangeDate=" + lastPwdChangeDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) +
                ", status=" + status +
                ", role=" + role +
                '}';
    }
}
