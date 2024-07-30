package com.backend.martall.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderState {
    // W -> P -> C 순
    ORDER_PREPARE("P"),
    ORDER_COMPLETE("C"),
    ORDER_WAIT("W"),
    ORDER_REFUSE("R")
    ;

    private final String code;

    public static boolean isValidState(String state) {
        for (OrderState orderState : OrderState.values()) {
            if (orderState.getCode().equals(state)) {
                return true;
            }
        }
        return false;
    }

}
