package com.backend.martall.domain.order.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInfoResponse {
    private Long martShopId;
    private String martName;
    private int itemCount;

}
