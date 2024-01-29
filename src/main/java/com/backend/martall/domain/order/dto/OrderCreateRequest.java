package com.backend.martall.domain.order.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderCreateRequest {

    private int payment;

    private List<OrderItemAddRequest> cartItemList;
}
