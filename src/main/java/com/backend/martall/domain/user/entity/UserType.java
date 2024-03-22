package com.backend.martall.domain.user.entity;

import com.backend.martall.domain.mart.entity.MartTag;
import com.backend.martall.global.exception.BadRequestException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

import static com.backend.martall.global.exception.ResponseStatus.ITEM_CATEGORY_NAME_WRONG;
import static com.backend.martall.global.exception.ResponseStatus.WRONG_USERTYPE;

@Getter
@AllArgsConstructor
public enum UserType {
    USER(1),
    MART(2)
    ;

    private final int typeCode;

    public static UserType findByTypeCode(int typeCode) {
        return Arrays.stream(UserType.values())
                .filter(userType -> userType.getTypeCode() == typeCode)
                .findFirst()
                .orElseThrow(() -> new BadRequestException(WRONG_USERTYPE));
    }
}
