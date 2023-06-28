package com.momodo.userApp.dto;


import com.momodo.userApp.domain.UserType;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RequestLogin {

    private UserType loginType;
    private String accessToken;

}
