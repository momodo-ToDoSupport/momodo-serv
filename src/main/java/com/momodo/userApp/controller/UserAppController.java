package com.momodo.userApp.controller;


import com.momodo.jwt.dto.BasicResponse;
import com.momodo.jwt.dto.DataResponse;
import com.momodo.userApp.dto.RequestCreateUserApp;
import com.momodo.userApp.dto.RequestUpdateUserProfile;
import com.momodo.userApp.dto.ResponseUserApp;
import com.momodo.userApp.service.UserAppService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name="App 회원", description = "APP 사용자에 관한 API 입니다.")
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user-app")
public class UserAppController {

    private final UserAppService userAppService;

    @Operation(summary = "회원 가입")
    @PostMapping
    public ResponseEntity<BasicResponse> signup(@Valid @RequestBody RequestCreateUserApp registerDto) {
        userAppService.signup(registerDto);

        return ResponseEntity.status(HttpStatus.CREATED).body(BasicResponse.of(HttpStatus.OK));
    }


    // @AuthenticationPricipal를 통해 JwtFilter에서 토큰을 검증하여 등록한 시큐리티 유저 객체를 꺼내올 수 있다.
    @Operation(summary = "현재 로그인한 User 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping
    public DataResponse<ResponseUserApp.Info> getMyUserAppInfo(@AuthenticationPrincipal User user) {
        ResponseUserApp.Info info = userAppService.getMyUserAppAuthorities();

        return DataResponse.of(info);
    }

    @Operation(summary = "Id로 User 정보 조회")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @GetMapping("/{userId}")
    public DataResponse<ResponseUserApp.Info> getUserAppInfo(@PathVariable String userId) {
        ResponseUserApp.Info info = userAppService.getUserAppWithAuthorities(userId);

        return DataResponse.of(info);
    }

    @Operation(summary = "User 프로필 수정")
    @PreAuthorize("hasAnyRole('MEMBER')")
    @PutMapping
    public BasicResponse updateProfile(@NotNull @RequestPart MultipartFile file, @Valid @RequestPart RequestUpdateUserProfile updateDto,
                                       @AuthenticationPrincipal User user)  {
        userAppService.updateProfile(user.getUsername(), file, updateDto);

        return BasicResponse.of(HttpStatus.OK);
    }
}
