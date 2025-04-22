package com.spring.refruitshop.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "login_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory {

    @Id
    @Column(name = "loginhis_no", nullable = false, updatable = false)
    @SequenceGenerator(name="SEQ_LOGIN_GENERATOR", sequenceName="loginhis_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_LOGIN_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no")
    private User user;

    @Column(name = "client_ip")
    private String clientIp;

    @Column(name = "loginhis_date")
    private LocalDateTime loginDate;

    @Builder
    public LoginHistory(User user, String clientIp) {
        this.user = user;
        this.clientIp = clientIp;
        this.loginDate = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "LoginHistory{" +
                "id=" + id +
                ", userid=" + user.getUserId() +
                ", userName=" + user.getName() +
                ", userEmail=" + user.getEmail() +
                ", userRole=" + user.getAuthorities() +
                ", clientIp='" + clientIp + '\'' +
                ", loginDate='" + loginDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) + '\'' +
                '}';
    }
}
