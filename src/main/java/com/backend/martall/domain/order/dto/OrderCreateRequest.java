package com.backend.martall.domain.order.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderCreateRequest {

    private int totalPayment;

    private String martShopId;

    private List<OrderItemCreateRequest> cartItemList;

}
