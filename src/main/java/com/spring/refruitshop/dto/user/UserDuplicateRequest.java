package com.spring.refruitshop.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDuplicateRequest {

    private String userId;
    private String email;
}
