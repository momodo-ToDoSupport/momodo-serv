package com.momodo.userApp.controller;


import com.momodo.jwt.dto.BasicResponse;
import com.momodo.jwt.dto.DataResponse;
import com.momodo.userApp.domain.UserType;
import com.momodo.userApp.dto.RequestLogin;
import com.momodo.userApp.dto.ResponseAuthentication;
import com.momodo.userApp.dto.google.ConfigUtils;
import com.momodo.userApp.service.SocialLoginService;
import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

@Tag(name="소셜 로그인", description = "소셜 로그인 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/social-login")
public class SocialController {

    private final SocialLoginService socialLoginService;
    private final ConfigUtils configUtils;

    @Operation(summary = "카카오 소셜 로그인")
    @PostMapping("/kakao")
    public DataResponse<ResponseAuthentication.Token> socialLogin(@RequestBody RequestLogin requestLogin) throws Exception {

        return socialLoginService.socialLogin(requestLogin);
    }

    /**
    @Hidden
    @GetMapping(value = "/login")
    public ResponseEntity<Object> moveGoogleInitUrl() {
        String authUrl = configUtils.googleInitUrl();
        URI redirectUri = null;
        try {
            redirectUri = new URI(authUrl);
            HttpHeaders httpHeaders = new HttpHeaders();
            httpHeaders.setLocation(redirectUri);
            return new ResponseEntity<>(httpHeaders, HttpStatus.SEE_OTHER);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        return ResponseEntity.badRequest().build();
    }

    @Operation(summary = "구글 소셜 로그인", description = "구글 로그인 기능입니다.")
    @GetMapping(value = "/google")
    public ResponseEntity<BasicResponse> redirectGoogleLogin1(
            @RequestParam(value = "code") String authCode
    ) throws Exception {
        RequestLogin requestLogin = RequestLogin.builder()
                .loginType(UserType.GOOGLE)
                .accessToken(authCode)
                .build();

        BasicResponse data = socialLoginService.socialLogin(requestLogin);
        return ResponseEntity.ok(data);
    }**/
}
