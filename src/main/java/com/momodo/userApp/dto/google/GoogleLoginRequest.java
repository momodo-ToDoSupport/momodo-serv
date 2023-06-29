package com.momodo.userApp.dto.google;

// 일회성 토큰을 받은 후, 해당 일회성 토큰을 가지고 AccessToken을 얻기 위한 Request VO
// lombok 사용

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoogleLoginRequest {

    private String clientId;  // 애플리케이션의 클라이언트 ID
    private String redirectUri;  //Google 로그인 후 redirect 위치
    private String clientSecret;  //애플리케이션의 클라이언트 비밀번호
    private String responseType;  // Google OAuth 2.0 엔드포인트가 인증 코드를 반환하는지 여부
    private String scope;  //OAuth 동의 범위
    private String code;
    private String accessType;  //사용자가 브라우저에 없을 때 애플리케이션이 액세스 토큰을 새로 고칠 수 있는지 여부
    private String grantType;  //인증 코드 교환을 위해 사용되는 인증 유형
    private String state;  //인증 요청 중에 애플리케이션이 생성한 상태 매개변수
    private String includeGrantedScopes;  //사용자가 동의한 범위에 추가로 액세스할 수 있는지 여부
    private String loginHint;  // 애플리케이션이 인증하려는 사용자를 알고 있는 경우 이 매개변수를 사용하여 Google 인증 서버에 힌트를 제공
    private String prompt;  // default: 처음으로 액세스를 요청할 때만 사용자에게 메시지가 표시

}
