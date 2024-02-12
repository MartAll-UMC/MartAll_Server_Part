package com.backend.martall.domain.item.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemCategory {
    // 한글 이름을 추가했습니다.
    SNACK("스낵"),
    DRINK("음료"),
    COFFEE("커피"),
    SALAD("샐러드"),
    CONVENIENT_FOOD("간편식품"),
    FROZEN_FOOD("냉동식품"),
    FRUIT("신선과일"),
    DAILY_SUPPLIES("생활용품?")
    ;

    private final String name;
}
