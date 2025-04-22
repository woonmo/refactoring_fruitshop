package com.spring.refruitshop.dto.user;

public interface UserIdEmailOnly {

    // 엔티티의 일부만 가져오는 Projection 인터페이스
    //

    String getUserId();
    String getEmail();
}
