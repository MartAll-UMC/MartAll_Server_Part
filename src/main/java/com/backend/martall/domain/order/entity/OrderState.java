package com.backend.martall.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum OrderState {
    ORDER_REQUEST("주문요청", "R"),
    ORDER_APPROVE("주문승인", "A"),
    ORDER_PACKAGING("포장중", "P"),
    ORDER_CANCEL("주문취소", "C"),
    ;

    private final String state;
    private final String code;

    public static String getStateByCode(String code) {
        OrderState returnState = Arrays.stream(OrderState.values())
                .filter(orderState -> orderState.code.equals(code))
                .findFirst().get();

        return returnState.state;
    }

    public static String getCodeByState(String state) {
        OrderState returnCode = Arrays.stream(OrderState.values())
                .filter(orderState -> orderState.state.equals(state))
                .findFirst().get();

        return returnCode.state;
    }
}
