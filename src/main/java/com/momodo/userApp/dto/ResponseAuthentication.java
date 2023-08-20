package com.momodo.userApp.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseAuthentication {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Token {
        private String accessToken;
        private String refreshToken;
    }
}
