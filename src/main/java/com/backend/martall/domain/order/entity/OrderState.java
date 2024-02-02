package com.backend.martall.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
@Getter
public enum OrderState {
    ORDER_PREPARE("상품준비중", "P"),
    ORDER_COMPLETE("상품준비완료", "C")
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
