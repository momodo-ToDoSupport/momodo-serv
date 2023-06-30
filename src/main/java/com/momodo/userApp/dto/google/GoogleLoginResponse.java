package com.momodo.userApp.dto.google;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GoogleLoginResponse {

    private String accessToken;  // 애플리케이션이 Google API 요청을 승인하기 위해 보내는 토큰
    private String expiresIn;  // 액세스 토큰의 유효 기간(초)
    private String refreshToken;  // 액세스 토큰이 만료된 후 새 액세스 토큰을 얻기 위해 사용되는 토큰
    private String scope;  // 액세스 토큰의 범위
    private String tokenType;  // 반환된 토큰 유형(Bearer 고정)
    private String idToken;

}
