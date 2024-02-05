package com.backend.martall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(true, 200, "요청이 성공했습니다"),

    // Success

    // Exception
    //4100 ~ 4199
    LOGIN_FAIL_EMPTY_PROVIDER(false, 4100, "provider 정보가 제공되지 않았습니다."),
    LOGIN_FAIL_EMPTY_PROVIDER_ID(false, 4101, "provider id가 제공되지 않았습니다."),
    LOGIN_FAIL_EXPIRED_JWT(false, 4102, "토큰이 만료되었습니다."),
    LOGIN_FAIL_WRONG_JWT(false, 4103, "잘못된 JWT입니다."),
    FAIL_ACCESS_EMPRY_JWT(false, 4104, "JWT를 입력해주세요."),



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
