package com.momodo.userApp.dto;

import com.momodo.userApp.domain.Role;
import com.momodo.userApp.domain.UserApp;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class ResponseUserApp {

    @Builder
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Info {
        private String userId;
        private String name;
        private String phone;
        private String tier;
        private Long tokenWeight;
        private Role roles;
        private String profileImage;
        private String introduce;

        public static ResponseUserApp.Info of(UserApp userApp, String imageUrl) {
            if(userApp == null) return null;

            return Info.builder()
                    .userId(userApp.getUserId())
                    .name(userApp.getName())
                    .phone(userApp.getPhone())
                    .tier(userApp.getTier())
                    .tokenWeight(userApp.getTokenWeight())
                    .roles(userApp.getRoles())
                    .profileImage(imageUrl)
                    .introduce(userApp.getIntroduce())
                    .build();
        }
    }
}
