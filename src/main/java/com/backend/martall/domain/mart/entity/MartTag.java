package com.backend.martall.domain.mart.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum MartTag {
    ALL("전체"),
    CHILD("육아용품"),
    COSMETIC("화장품"),
    FOOD("식품"),
    SEAFOOD("수산"),
    HEALTH("건강식품"),
    PET("반려동물"),
    MEAT("정육")
    ;

    private final String name;

    public static boolean existByName(String name) {
        return Arrays.stream(MartTag.values())
                .anyMatch(martTag -> martTag.getName().equals(name));
    }
}
