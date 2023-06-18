package com.momodo.userApp.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum UserType {
    KAKAO("카카오"),
    GOOGLE("구글"),
    MOMODO("모모도");

    private final String title;

    public String getCode() {
        return name();
    }

    public String getTitle() {
        return title;
    }

}
