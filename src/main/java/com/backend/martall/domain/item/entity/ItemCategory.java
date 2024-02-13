package com.backend.martall.domain.item.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemCategory {
    // 한글 이름을 추가했습니다.
    FRUTVEG("과일&채소"),
    SEAFOOD("수산"),
    MEAT("정육"),
    SNACK("간식")
    ;

    private final String name;
}
