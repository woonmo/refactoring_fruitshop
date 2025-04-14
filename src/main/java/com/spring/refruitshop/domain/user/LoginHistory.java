package com.spring.refruitshop.domain.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "login_history")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LoginHistory {

    @Id
    @Column(unique = true, nullable = false, name = "loginhis_no")
    @SequenceGenerator(name="SEQ_LOGIN_GENERATOR", sequenceName="loginhis_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="SEQ_LOGIN_GENERATOR")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no")
    private User user;

    @Column(name = "client_ip")
    private String clientIp;


    @Builder
    public LoginHistory(User user, String clientIp) {
        this.user = user;
        this.clientIp = clientIp;
    }

    @Override
    public String toString() {
        return "LoginHistory{" +
                "id=" + id +
                ", userid=" + user.getUserId() +
                ", userName=" + user.getName() +
                ", userEmail=" + user.getEmail() +
                ", clientIp='" + clientIp + '\'' +
                '}';
    }
}
