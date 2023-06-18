package com.momodo.userApp.controller;


import com.momodo.jwt.dto.CommonResponse;
import com.momodo.jwt.security.JwtFilter;
import com.momodo.userApp.dto.RequestUserApp;
import com.momodo.userApp.dto.ResponseAuthentication;
import com.momodo.userApp.service.AuthenticationService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Tag(name="인증 인가", description = "인증 인가 관한 API 입니다.")
@RestController
@RequestMapping("/api/v1/authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    public AuthenticationController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/token")  // UserAdmin 인증 API
    public ResponseEntity<CommonResponse> authorize(@Valid @RequestBody RequestUserApp.Login loginDto) {

        ResponseAuthentication.Token token = authenticationService.authenticate(loginDto.getUserId(), loginDto.getPassword());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(token)
                .build();

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    @PutMapping("/token")  // 리프레시 토큰을 활용한 액세스 토큰 갱신
    public ResponseEntity<CommonResponse> refreshToken(@Valid @RequestBody RequestUserApp.Refresh refreshDto) {

        ResponseAuthentication.Token token = authenticationService.refreshToken(refreshDto.getToken());

        // response header 에도 넣고 응답 객체에도 넣는다.
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add(JwtFilter.AUTHORIZATION_HEADER, "Bearer " + token.getAccessToken());

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .response(token)
                .build();

        return new ResponseEntity<>(response, httpHeaders, HttpStatus.OK);
    }

    // 리프레시토큰 만료 API
    // -> 해당 계정의 가중치를 1 올린다. 그럼 나중에 해당 리프레시 토큰으로 갱신 요청이 들어와도 받아들여지지 않는다.
    @DeleteMapping("/{userId}/token")
    @PreAuthorize("hasAnyRole('SUPER')")  // SUPER 권한만 호출 가능
    public ResponseEntity<CommonResponse> authorize(@PathVariable String userId) {
        authenticationService.invalidateRefreshTokenByUsername(userId);

        // 응답
        CommonResponse response = CommonResponse.builder()
                .success(true)
                .build();

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 계정 탈퇴 구현 시 계정을 삭제하지 않고 비활성화 시켜야한다. field activated
}
