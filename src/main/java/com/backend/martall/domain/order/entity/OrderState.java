package com.backend.martall.domain.order.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum OrderState {
    ORDER_PREPARE("P"),
    ORDER_COMPLETE("C")
    ;

    private final String code;

}
