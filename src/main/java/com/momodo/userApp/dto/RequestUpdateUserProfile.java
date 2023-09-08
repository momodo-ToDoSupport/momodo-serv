package com.momodo.userApp.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RequestUpdateUserProfile {

    @Schema(description = "이름")
    @NotBlank
    private String name;

    @Schema(description = "자기소개")
    private String introduce;
}
