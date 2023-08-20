package com.momodo.userApp.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TodoTier {
    RED(0),
    GREEN(1),
    BLUE(2),
    RAINBOW(3);

    private final Integer tier;

    public Integer getTier() {
        return tier;
    }
}
