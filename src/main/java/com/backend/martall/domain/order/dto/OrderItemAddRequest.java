package com.backend.martall.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemAddRequest {
    private Long cartItemId;
}
