package com.backend.martall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(true, 200, "요청이 성공했습니다"),

    // Success

    // Exception

    NO_SELECT_IMAGE(false, 4150, "선택된 이미지가 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL(false, 4151, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAIL(false, 4152, "이미지 삭제에 실패했습니다"),
    INVALID_IMAGE_URL(false, 4153, "잘못된 이미지 URL입니다."),


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
