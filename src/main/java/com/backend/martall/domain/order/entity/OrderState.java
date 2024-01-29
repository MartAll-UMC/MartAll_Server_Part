package com.backend.martall.domain.order.entity;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum OrderState {
    ORDER_REQUEST("주문요청"),
    ORDER_APPROVE("주문승인"),
    ORDER_PACKAGING("포장중"),
    ORDER_CANCEL("주문취소"),
    ;

    private final String state;

}
