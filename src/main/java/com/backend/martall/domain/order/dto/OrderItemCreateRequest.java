package com.backend.martall.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemCreateRequest {
    private Long cartItemId;
    private int itemId;
    private String martShopId;
    private int count;

}
