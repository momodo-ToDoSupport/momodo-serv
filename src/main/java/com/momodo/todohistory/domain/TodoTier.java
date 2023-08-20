package com.momodo.todohistory.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TodoTier {
    RED(0),
    GREEN(3),
    BLUE(10),
    RAINBOW(20);

    private final Integer tier;

    public Integer getTier() {
        return tier;
    }
}
