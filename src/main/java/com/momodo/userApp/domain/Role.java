package com.momodo.userApp.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Role {
    MEMBER("회원");

    private final String title;

    public String getCode() {
        return name();
    }

    public String getTitle() {
        return title;
    }

}
