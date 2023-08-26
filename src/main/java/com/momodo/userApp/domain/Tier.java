package com.momodo.userApp.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Tier {
    RED(0),
    GREEN(3),
    BLUE(10),
    RAINBOW(20);

    private final Integer tier;

    public Integer getTier() {
        return tier;
    }
}
