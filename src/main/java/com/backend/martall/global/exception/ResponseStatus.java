package com.backend.martall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(true, 200, "요청이 성공했습니다"),

    // Success

    // Exception
    // 4220 ~ 상품 찜하기 에러
    ITEMLIKE_ALREADY_LIKE(false, 4220, "이미 찜한 상품입니다."),
    ITEMLIKE_ALREADY_DISLIKE(false, 4221, "찜이 되어 있는 상품을 찜 취소했습니다."),
    ITEMLIKE_NOT_EXIST(false, 4222, "찜 상품 목록이 존재하지 않습니다."),

    // 5000 - Request Error
    REQUEST_ERROR(false, 5000, "잘못된 요청입니다."),

    // 6000 - Response Error
    RESPONSE_ERROR(false, 6000, "값을 불러오는데 실패했습니다"),


    // 7000 - Server Connection Error
    SERVER_ERROR(false, 7000, "서버 연결에 실패했습니다."),
    DATABASE_ERROR(false, 7100, "데이터베이스 오류가 발생했습니다."),
    ;

    private final boolean isSuccess;
    private final int code;
    private final String message;
}
