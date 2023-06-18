package com.momodo.userApp.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class RequestUserApp {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Login {
        @NotNull
        @Size(min = 3, max = 50)
        private String userId;

        @NotNull
        @Size(min = 5, max = 100)
        private String password;

    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Refresh {
        @NotNull
        private String token;
    }

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Register {
        @NotNull
        @Size(min = 3, max = 50)
        private String adminId;

        @NotNull
        @Size(min = 3, max = 100)
        private String password;

        @NotNull
        private String name;

        @NotNull
        private String phone;

        // TODO: 소속 제휴 추가필요
    }

}
