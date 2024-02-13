package com.backend.martall.global.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ResponseStatus {
    SUCCESS(true, 200, "요청이 성공했습니다"),

    // Success

    // 4200 ~ 상품
    ITEM_DETAIL_FAIL(false, 4200, "해당 상품을 찾을 수 없습니다. 마트 아이디나 상품 아이디를 확인해주세요"),

    // Exception
    //4100 ~ 유저
    LOGIN_FAIL_EMPTY_PROVIDER(false, 4100, "provider 정보가 제공되지 않았습니다."),
    LOGIN_FAIL_EMPTY_PROVIDER_ID(false, 4101, "provider id가 제공되지 않았습니다."),
    LOGIN_FAIL_EXPIRED_JWT(false, 4102, "토큰이 만료되었습니다."),
    LOGIN_FAIL_WRONG_JWT(false, 4103, "잘못된 JWT입니다."),
    FAIL_ACCESS_EMPRY_JWT(false, 4104, "JWT를 입력해주세요."),
    NOT_EXIST_USER(false, 4105, "존재하지 않는 유저입니다."),

    // 4150 ~ 이미지
    NO_SELECT_IMAGE(false, 4150, "선택된 이미지가 존재하지 않습니다."),
    IMAGE_UPLOAD_FAIL(false, 4151, "이미지 업로드에 실패했습니다."),
    IMAGE_DELETE_FAIL(false, 4152, "이미지 삭제에 실패했습니다"),
    INVALID_IMAGE_URL(false, 4153, "잘못된 이미지 URL입니다."),

    // 4220 ~ 상품 찜하기 에러
    ITEMLIKE_ALREADY_LIKE(false, 4220, "이미 찜한 상품입니다."),
    ITEMLIKE_ALREADY_DISLIKE(false, 4221, "찜이 되어 있지 않은 상품을 찜 취소했습니다."),
    ITEMLIKE_NOT_EXIST(false, 4222, "찜 상품 목록이 존재하지 않습니다."),
    ITEMLIKE_ITEM_NOT_EXIST(false, 4223, "찜하려는 상품이 존재하지 않는 상품입니다."),

    //4300 ~ 마트
    MART_NAME_NOT_FOUND(false, 4300, "마트를 찾을 수 없습니다."),
    MART_ACCESS_DENIED(false, 4301, "마트 접근 권한이 없습니다."),
    MART_OPERATION_ERROR(false, 4302, "마트 작업 중 오류가 발생했습니다."),
    MART_FAVORITE_ALREADY(false, 4303, "이미 단골 추가된 마트 입니다."),
    MART_DETAIL_FAIL(false, 4304, "마트 상세정보를 불러 올 수 없습니다."),
    MART_CREATE_FAIL(false, 4305, "마트 생성에 실패했습니다."),
    MART_FILTER_NOT_FOUND(false, 4306, "해당하는 마트를 찾을 수 없습니다."),
    MART_CATEGORY_NOT_FOUND(false, 4307, "해당하는 마트 카테고리를 찾을 수 없습니다."),
    MART_FAVORITE_NOT_FOUND(false, 4308, "단골 취소하려는 마트를 찾을 수 없습니다."),
    // 4400 ~ 4499
    // 장바구니 4400 ~
    CART_ITEM_NOT_EXIST(false, 4400, "장바구니에 상품이 존재하지 않습니다."),
    CART_USER_NOT_EQUAL(false, 4401, "장바구니 상품은 관련 회원만 접근 할 수 있습니다."),
    CART_MART_NOT_EQUAL(false, 4402, "장바구니에는 같은 마트의 상품만 넣을 수 있습니다."),

    // 주문 4420 ~
    ORDER_PAYMENT_NOT_EQUAL(false, 4420, "결제 금액이 올바르지 않습니다."),
    ORDER_EXIST_OTHER_ORDER(false, 4421, "다른 주문이 존재합니다."),
    ORDER_NOT_EXIST(false, 4422, "주문이 존재하지 않습니다."),
    ORDER_CART_EMPTY(false, 4423, "주문하려는 장바구니 상품 목록이 비어있습니다."),
    ORDER_CARTITEM_NOT_EXIST(false, 4424, "장바구니에 존재하지 않는 상품입니다."),

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