package com.momodo.userApp.dto;

import com.momodo.userApp.domain.UserType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "회원(유저) 등록 DTO")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestCreateUserApp {

    @Schema(description = "아이디")
    private String userId;

    @Schema(description = "계정구분")
    private UserType type;

    @Schema(description = "비밀번호")
    private String password;

    @Schema(description = "이름")
    private String name;

    @Schema(description = "이메일")
    private String email;

    @Schema(description = "전화번호")
    private String phone;

}
