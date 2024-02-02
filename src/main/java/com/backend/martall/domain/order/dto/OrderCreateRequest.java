package com.backend.martall.domain.order.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

    private int totalPayment;

    private String martShopId;

    private List<OrderItemCreateRequest> cartItemList;

}
