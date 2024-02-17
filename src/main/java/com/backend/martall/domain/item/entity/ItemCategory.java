package com.backend.martall.domain.item.entity;

import com.backend.martall.global.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.backend.martall.global.exception.ResponseStatus.ITEM_CATEGORY_NAME_WRONG;

@Getter
@AllArgsConstructor
public enum ItemCategory {
    // 한글 이름을 추가했습니다.
    ALL("전체"),
    FRUTVEG("과일&채소"),
    SEAFOOD("수산"),
    MEAT("정육"),
    SNACK("간식")
    ;

    private final String name;

    public static ItemCategory findByName(String name) {
        return Arrays.stream(ItemCategory.values())
                .filter(itemCategory -> itemCategory.name.equals(name))
                .findAny()
                .orElseThrow(() -> new BadRequestException(ITEM_CATEGORY_NAME_WRONG));
    }
}
