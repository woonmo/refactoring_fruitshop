package com.spring.refruitshop.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserDuplicateResponse {

    @JsonProperty("isUserIdExist")
    private boolean isUserIdExist;

    @JsonProperty("isEmailExist")
    private boolean isEmailExist;

    public UserDuplicateResponse (boolean userIdExist, boolean emailExist) {
        this.isUserIdExist = userIdExist;
        this.isEmailExist = emailExist;
    }

    @Override
    public String toString() {
        return "UserDuplicateResponse{" +
                "isUserIdExist=" + isUserIdExist +
                ", isEmailExist=" + isEmailExist +
                '}';
    }
}
