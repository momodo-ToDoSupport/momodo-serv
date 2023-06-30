package com.momodo.userApp.controller;


import com.momodo.jwt.dto.CommonResponse;
import com.momodo.userApp.dto.RequestCreateUserApp;
import com.momodo.userApp.dto.ResponseUserApp;
import com.momodo.userApp.service.UserAppService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

@Tag(name="App 회원", description = "APP 사용자에 관한 API 입니다.")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-app")
public class UserAppController {

    private final UserAppService userAppService;

    @PostMapping
    public ResponseEntity<CommonResponse> signup(@Valid @RequestBody RequestCreateUserApp registerDto) {
        CommonResponse data = userAppService.signup(registerDto);
        return ResponseEntity.ok(data);
    }

    /**
     * 인가 테스트
     * Authorization: Bearer {AccessToken}
     *
     * @AuthenticationPricipal를 통해 JwtFilter에서 토큰을 검증하여 등록한 시큐리티 유저 객체를 꺼내올 수 있다.
     * JwtFilter는 디비 조회를 하지 않기에 유저네임, 권한만 알 수 있음
     * Account 엔티티에 대한 정보를 알고 싶으면 디비 조회를 별도로 해야 한다.
     */
    @GetMapping
    public ResponseEntity<ResponseUserApp.Info> getMyUserAppInfo(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(userAppService.getMyUserAppAuthorities());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ResponseUserApp.Info> getUserAppInfo(@PathVariable String userId) {
        return ResponseEntity.ok(userAppService.getUserAppWithAuthorities(userId));
    }

}
